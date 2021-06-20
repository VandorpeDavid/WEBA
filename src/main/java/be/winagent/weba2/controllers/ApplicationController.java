package be.winagent.weba2.controllers;

import be.winagent.weba2.controllers.exceptions.NotFoundException;
import be.winagent.weba2.domain.models.*;
import be.winagent.weba2.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

public abstract class ApplicationController {
    @Autowired
    private AssociationService associationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private AuthenticationService authenticationService;

    @ModelAttribute(binding = false)
    public Association addAssociation(@RequestParam(required = false, name = "association") String abbreviation) {
        return Optional.ofNullable(abbreviation)
                .map(
                        (abbrev) -> associationService
                                .findByAbbreviation(abbrev)
                                .orElseThrow(NotFoundException::new)
                )
                .orElse(null);
    }

    @ModelAttribute(binding = false)
    public Event addEvent(@RequestParam(required = false, name = "event") UUID eventId) {
        return Optional.ofNullable(eventId)
                .map(
                        (id) -> eventService
                                .find(id)
                                .orElseThrow(NotFoundException::new)
                )
                .orElse(null);
    }

    @ModelAttribute(binding = false)
    public Item addItem(@RequestParam(required = false, name = "item") Long itemId) {
        return Optional.ofNullable(itemId)
                .map(
                        (id) -> itemService
                                .find(id)
                                .orElseThrow(NotFoundException::new)
                )
                .orElse(null);
    }

    @ModelAttribute(binding = false)
    public Table addTable(@RequestParam(required = false, name = "table") UUID tableId) {
        return Optional.ofNullable(tableId)
                .map(
                        (id) -> tableService
                                .find(id)
                                .orElseThrow(NotFoundException::new)
                )
                .orElse(null);
    }

    @ModelAttribute(binding = false)
    public Order addOrder(@RequestParam(required = false, name = "order") UUID orderId) {
        return Optional.ofNullable(orderId)
                .map(
                        (id) -> orderService
                                .find(id)
                                .orElseThrow(NotFoundException::new)
                )
                .orElse(null);
    }

    @ModelAttribute(binding = false, name = "location")
    public Location addLocation(@RequestParam(required = false, name = "location") UUID locationId) {
        return Optional.ofNullable(locationId)
                .map(
                        (id) -> locationService
                                .find(id)
                                .orElseThrow(NotFoundException::new)
                )
                .orElse(null);
    }

    @ModelAttribute(binding = false, name = "currentUser")
    public User addCurrentUser(Authentication authentication) {
        return authenticationService.getUser(authentication).orElse(null);
    }

    protected void setActiveAssociation(Model model, Association association) {
        model.addAttribute("activeAssociation", association);
    }
}
