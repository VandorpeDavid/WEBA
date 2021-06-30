package be.winagent.weba2.controllers;

import be.winagent.weba2.controllers.annotations.Required;
import be.winagent.weba2.controllers.forms.converter.AdminEventConverter;
import be.winagent.weba2.controllers.forms.converter.BidirectionalConverter;
import be.winagent.weba2.controllers.forms.models.EventForm;
import be.winagent.weba2.domain.models.*;
import be.winagent.weba2.services.EventService;
import be.winagent.weba2.services.LocationService;
import be.winagent.weba2.services.OrderService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController extends ApplicationController {
    private final EventService eventService;
    private final BidirectionalConverter<Event, EventForm> formConverter;
    private final AdminEventConverter adminFormConverter;
    private final LocationService locationService;

    public EventController(
            EventService eventService,
            @Qualifier("eventConverter") BidirectionalConverter<Event, EventForm> formConverter,
            AdminEventConverter adminFormConverter, LocationService locationService) {
        this.eventService = eventService;
        this.formConverter = formConverter;
        this.adminFormConverter = adminFormConverter;
        this.locationService = locationService;
    }

    @GetMapping("/show")
    @PreAuthorize("isEventAdmin(#event)")
    public String show(Model model, @Required Event event) {
        setActiveAssociation(model, event.getAssociation());

        event.getTables().sort(Comparator.comparing(Table::getName));
        event.getItems().sort(Comparator.comparing(Item::getName));
        return "events/show";
    }

    @GetMapping(value = "/qrcodes", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("isEventAdmin(#event)")
    public ResponseEntity<InputStreamResource> qrcodes(@Required Event event) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("inline; filename=qrcodes-%s.pdf", event.getId()));
        ByteArrayInputStream in = eventService.generateTablePdf(event);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }

    @GetMapping("/create")
    @PreAuthorize("isAdmin()")
    public String createForm(@Required Association association, EventForm eventForm, Model model) {
        model.addAttribute("locations", locationService.all());
        setActiveAssociation(model, association);

        return "events/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAdmin()")
    public String create(@Valid EventForm eventForm, BindingResult bindingResult, @Required Association association, Model model) {

        if (bindingResult.hasErrors()) {
            setActiveAssociation(model, association);
            model.addAttribute("locations", locationService.all());
            return "events/create";
        }

        Event event = adminFormConverter.build(eventForm);
        event.setAssociation(association);
        event = eventService.create(event);
        return redirect(event);
    }

    @GetMapping("/edit")
    @PreAuthorize("isEventAdmin(#event)")
    public String editForm(Model model, @Required Event event) {
        model.addAttribute("locations", locationService.all());
        model.addAttribute("eventForm", formConverter.reverseBuild(event));
        setActiveAssociation(model, event.getAssociation());
        return "events/edit";
    }

    @GetMapping("/orderedItems")
    @PreAuthorize("isEventAdmin(#event)")
    public String orderedItems(Model model, @Required Event event) {
        setActiveAssociation(model, event.getAssociation());
        Map<String, Map<Integer, Integer>> items = event.getOrders().stream()
                .map(Order::getItems)
                .flatMap(List::stream)
                .collect(
                        Collectors.groupingBy(
                                OrderItem::getName,
                                Collectors.groupingBy(
                                        OrderItem::getPrice,
                                        Collectors.summingInt(
                                                OrderItem::getAmount

                                        )
                                )
                        )
                );

        model.addAttribute("itemsByNameAndPrice", items);

        event.getTables().sort(Comparator.comparing(Table::getName));
        event.getItems().sort(Comparator.comparing(Item::getName));
        return "events/orderedItems";
    }

    @PostMapping("/edit")
    @PreAuthorize("isEventAdmin(#event)")
    public String edit(@Valid EventForm eventForm, BindingResult bindingResult, @Required Event event, @ModelAttribute(name = "currentUser") User user, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("locations", locationService.all());
            setActiveAssociation(model, event.getAssociation());

            return "events/edit";
        }

        if (user.isAdmin()) {
            event = adminFormConverter.update(event, eventForm);
        } else {
            event = formConverter.update(event, eventForm);
        }
        event = eventService.update(event);
        return redirect(event);
    }

    public static String redirect(Event event) {
        return redirect(event, "show");
    }

    public static String redirect(Event event, String action) {
        return String.format("redirect:/events/%s?event=%s", action, event.getId());
    }
}
