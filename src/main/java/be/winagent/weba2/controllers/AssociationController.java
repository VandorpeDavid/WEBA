package be.winagent.weba2.controllers;

import be.winagent.weba2.controllers.annotations.Required;
import be.winagent.weba2.controllers.forms.converter.BidirectionalConverter;
import be.winagent.weba2.controllers.forms.models.AssociationForm;
import be.winagent.weba2.domain.models.Association;
import be.winagent.weba2.domain.models.User;
import be.winagent.weba2.services.AssociationService;
import be.winagent.weba2.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/associations")
public class AssociationController extends ApplicationController {
    private final AssociationService associationService;
    private final UserService userService;
    private final BidirectionalConverter<Association, AssociationForm> associationConverter;
    private final BidirectionalConverter<Association, AssociationForm> adminAssociationConverter;

    public AssociationController(
            AssociationService associationService,
            UserService userService,
            @Qualifier("associationConverter") BidirectionalConverter<Association, AssociationForm> associationConverter,
            @Qualifier("adminAssociationConverter") BidirectionalConverter<Association, AssociationForm> adminAssociationConverter) {
        this.associationService = associationService;
        this.userService = userService;
        this.associationConverter = associationConverter;
        this.adminAssociationConverter = adminAssociationConverter;
    }

    @GetMapping
    @PreAuthorize("isAdmin()")
    public String index(Model model) {
        model.addAttribute("associations", associationService.all());
        return "associations/index";
    }

    @GetMapping("/show")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String show(Model model, @Required Association association) {
        setActiveAssociation(model, association);
        return "associations/show";
    }

    @GetMapping("/create")
    @PreAuthorize("isAdmin()")
    public String createForm(AssociationForm associationForm) {
        return "associations/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAdmin()")
    public String create(@Valid AssociationForm associationForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "associations/create";
        }

        Association association = associationService.create(associationConverter.build(associationForm));
        return redirect(association);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String editForm(Model model, @Required Association association) {
        setActiveAssociation(model, association);
        model.addAttribute("associationForm", associationConverter.reverseBuild(association));
        return "associations/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String edit(Model model, @Valid AssociationForm associationForm, BindingResult bindingResult, @Required Association association, @ModelAttribute(name = "currentUser") User user) {
        setActiveAssociation(model, association);

        if (bindingResult.hasErrors()) {
            return "associations/edit";
        }

        if (user.isAdmin()) {
            association = adminAssociationConverter.update(association, associationForm);
        } else {
            association = associationConverter.update(association, associationForm);
        }

        association = associationService.update(association);
        return redirect(association);
    }

    @PostMapping("/addAdmin")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String addAdmin(RedirectAttributes model, @Required Association association, @RequestParam String username) {
        Optional<User> userOptional = userService.findByUsername(username);

        if(userOptional.isEmpty()) {
            model.addFlashAttribute("dangerAlert", String.format("Gebruikersnaam '%s' niet gevonden.", username));
            return redirect(association);
        }
        associationService.addAdmin(association, userOptional.get());
        return redirect(association);
    }

    @PostMapping("/deleteAdmin")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String deleteAdmin(RedirectAttributes model, @Required Association association, @RequestParam String username, @ModelAttribute(name = "currentUser") User currentUser) {
        if (currentUser.getUsername().equals(username)) {
            model.addFlashAttribute("dangerAlert", "Je kan jezelf niet als admin verwijderen.");
            return redirect(association);
        }
        associationService.removeAdmin(association, username);
        return redirect(association);
    }

    private String redirect(Association association) {
        return String.format("redirect:/associations/show?association=%s", association.getAbbreviation());
    }
}
