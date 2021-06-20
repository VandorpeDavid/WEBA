package be.winagent.weba2.controllers;

import be.winagent.weba2.controllers.annotations.Required;
import be.winagent.weba2.controllers.forms.converter.BidirectionalConverter;
import be.winagent.weba2.controllers.forms.models.ItemForm;
import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Item;
import be.winagent.weba2.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController extends ApplicationController {
    private final ItemService itemService;
    private final BidirectionalConverter<Item, ItemForm> formConverter;

    @GetMapping("/create")
    @PreAuthorize("isEventAdmin(#event)")
    public String createForm(@Required Event event, ItemForm itemForm) {
        return "items/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isEventAdmin(#event)")
    public String create(@Valid ItemForm itemForm, BindingResult bindingResult, @Required Event event) {
        if (bindingResult.hasErrors()) {
            return "items/create";
        }

        Item item = formConverter.build(itemForm);
        item = itemService.create(event, item);
        return redirect(item);
    }

    @GetMapping("/edit")
    @PreAuthorize("isItemAdmin(#item)")
    public String editForm(Model model, Item item) {
        model.addAttribute("itemForm", formConverter.reverseBuild(item));
        return "items/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("isItemAdmin(#item)")
    public String edit(@Valid ItemForm itemForm, BindingResult bindingResult, @Required Item item) {
        if (bindingResult.hasErrors()) {
            return "items/edit";
        }

        item = itemService.update(formConverter.update(item, itemForm));
        return redirect(item);
    }

    public String redirect(Item item) {
        return EventController.redirect(item.getEvent());
    }
}
