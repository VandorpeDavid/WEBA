package be.winagent.weba2.controllers;

import be.winagent.weba2.controllers.annotations.Required;
import be.winagent.weba2.controllers.forms.converter.BidirectionalConverter;
import be.winagent.weba2.controllers.forms.models.LocationForm;
import be.winagent.weba2.controllers.forms.models.TableForm;
import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.domain.models.Table;
import be.winagent.weba2.services.EventService;
import be.winagent.weba2.services.LocationService;
import be.winagent.weba2.services.TableService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/locations")
@AllArgsConstructor
public class LocationController extends ApplicationController {
    private final LocationService locationService;
    private final EventService eventService;
    private final BidirectionalConverter<Location, LocationForm> formConverter;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String index(Model model) {
        model.addAttribute("locations", locationService.all());
        return "locations/index";
    }

    @GetMapping("show")
    @PreAuthorize("isAuthenticated()")
    public String show(Location location) {
        return "locations/show";
    }

    @GetMapping("events")
    @PreAuthorize("isAdmin()")
    public String pastEvents(Location location, Pageable pageable, Model model, @RequestParam(required = false) Boolean past) {
        if (past == null) {
            past = false;
        }
        model.addAttribute("past", past);
        Page<Event> events;
        if(past) {
            events = eventService.getPastEvents(pageable, location);
        } else {
            events = eventService.getFutureEvents(pageable, location);
        }
        model.addAttribute("events", events);

        return "locations/events";
    }

    @GetMapping("/create")
    @PreAuthorize("isAdmin()")
    public String createForm(LocationForm locationForm) {
        return "locations/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAdmin()")
    public String create(@Valid LocationForm locationForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "locations/create";
        }

        Location location = formConverter.build(locationForm);
        location = locationService.create(location);
        return redirect(location);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAdmin()")
    public String editForm(Model model, Location location) {
        model.addAttribute("locationForm", formConverter.reverseBuild(location));
        return "locations/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("isAdmin()")
    public String edit(@Valid LocationForm locationForm, BindingResult bindingResult, @Required Location location) {
        if (bindingResult.hasErrors()) {
            return "locations/edit";
        }

        location = locationService.update(formConverter.update(location, locationForm));
        return redirect(location);
    }

    @DeleteMapping
    @PreAuthorize("isAdmin()")
    public String delete(@Required Location location) {
        locationService.remove(location);
        return "redirect:/locations/";
    }

    public static String redirect(Location location) {
        return String.format("redirect:/locations/show?location=%s", location.getId());
    }
}
