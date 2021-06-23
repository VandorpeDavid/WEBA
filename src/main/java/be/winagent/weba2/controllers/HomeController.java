package be.winagent.weba2.controllers;

import be.winagent.weba2.domain.models.Association;
import be.winagent.weba2.domain.models.User;
import be.winagent.weba2.security.annotations.Authenticated;
import be.winagent.weba2.services.AssociationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class HomeController extends ApplicationController {
    private final AssociationService associationService;

    @GetMapping("/")
    @Authenticated
    public String home(@ModelAttribute(name = "currentUser") User currentUser, Model model) {
        if (currentUser.isAdmin()) {
            return "redirect:/associations";
        }

        List<Association> adminAssociations = associationService.all()
                .stream()
                .filter((association) -> association.getAdmins().contains(currentUser))
                .collect(Collectors.toList());

        if (adminAssociations.size() == 1) {
            return "redirect:/associations/show?association=" + adminAssociations.get(0).getAbbreviation();
        }

        model.addAttribute("adminAssociations", adminAssociations);
        return "home";
    }

    @GetMapping("/info")
    @Authenticated
    public String info(@RequestParam(required = false) String lang, @ModelAttribute(name = "currentUser") User currentUser) {
        if (lang != null && lang.equals("en")) return "info_en";
        return "info_nl";
    }
}
