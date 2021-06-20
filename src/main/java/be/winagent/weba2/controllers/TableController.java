package be.winagent.weba2.controllers;

import be.winagent.weba2.controllers.annotations.Required;
import be.winagent.weba2.controllers.forms.converter.BidirectionalConverter;
import be.winagent.weba2.controllers.forms.models.TableForm;
import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.domain.models.Table;
import be.winagent.weba2.services.TableService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/tables")
@AllArgsConstructor
public class TableController extends ApplicationController {
    private final TableService tableService;
    private final BidirectionalConverter<Table, TableForm> formConverter;

    @GetMapping("/create")
    @PreAuthorize("isAdmin()")
    public String createForm(@Required Location location, TableForm tableForm) {
        return "tables/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAdmin()")
    public String create(@Valid TableForm tableForm, BindingResult bindingResult, @Required Location location) {
        if (bindingResult.hasErrors()) {
            return "tables/create";
        }

        Table table = formConverter.build(tableForm);
        table = tableService.create(location, table);
        return redirect(table);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAdmin()")
    public String editForm(Model model, Table table) {
        model.addAttribute("tableForm", formConverter.reverseBuild(table));
        return "tables/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("isAdmin()")
    public String edit(@Valid TableForm tableForm, BindingResult bindingResult, @Required Table table) {
        if (bindingResult.hasErrors()) {
            return "tables/edit";
        }

        table = tableService.update(formConverter.update(table, tableForm));
        return redirect(table);
    }

    @DeleteMapping
    @PreAuthorize("isAdmin()")
    public String delete(@Required Table table) {
        Location location = table.getLocation();
        tableService.remove(table);
        return LocationController.redirect(location);
    }


    public String redirect(Table table) {
        return LocationController.redirect(table.getLocation());
    }
}
