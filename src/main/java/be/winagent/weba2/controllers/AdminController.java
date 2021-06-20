package be.winagent.weba2.controllers;

import be.winagent.weba2.controllers.annotations.Required;
import be.winagent.weba2.controllers.exceptions.NotFoundException;
import be.winagent.weba2.controllers.forms.converter.BidirectionalConverter;
import be.winagent.weba2.controllers.forms.models.AssociationForm;
import be.winagent.weba2.domain.models.Association;
import be.winagent.weba2.domain.models.User;
import be.winagent.weba2.services.AssociationService;
import be.winagent.weba2.services.UserService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/admins")
@AllArgsConstructor
public class AdminController extends ApplicationController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAdmin()")
    public String index(Model model) {
        model.addAttribute("admins", userService.allAdmins());
        return "admins/index";
    }

    @PostMapping("/addAdmin")
    @PreAuthorize("isAdmin()")
    public String addAdmin(RedirectAttributes model, @Required @RequestParam String username) {
        Optional<User> userOptional = userService.findByUsername(username);

        if(userOptional.isEmpty()) {
            model.addFlashAttribute("dangerAlert", String.format("Gebruikersnaam '%s' niet gevonden.", username));
            return redirect();
        }
        User user = userOptional.get();
        userService.makeAdmin(user);
        return redirect();
    }

    @PostMapping("/deleteAdmin")
    @PreAuthorize("isAdmin()")
    public String deleteAdmin(RedirectAttributes model, @RequestParam String username, @ModelAttribute(name = "currentUser") User currentUser) {
        if(currentUser.getUsername().equals(username)) {
            model.addFlashAttribute("dangerAlert", "Je kan jezelf niet als admin verwijderen.");
            return redirect();
        }
        Optional<User> userOptional = userService.findByUsername(username);

        if(userOptional.isEmpty()) {
            model.addFlashAttribute("dangerAlert", String.format("Gebruikersnaam '%s' niet gevonden.", username));
            return redirect();
        }

        User user = userOptional.get();
        userService.removeAdmin(user);
        return redirect();
    }

    private String redirect() {
        return "redirect:/admins";
    }
}
