package be.winagent.weba2.controllers;

import be.winagent.weba2.controllers.annotations.Required;
import be.winagent.weba2.controllers.forms.converter.Converter;
import be.winagent.weba2.controllers.forms.models.OrderForm;
import be.winagent.weba2.controllers.forms.models.OrderItemForm;
import be.winagent.weba2.domain.models.*;
import be.winagent.weba2.services.EventService;
import be.winagent.weba2.services.LocationService;
import be.winagent.weba2.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController extends ApplicationController {
    private final OrderService orderService;
    private final Converter<Order, OrderForm> formConverter;
    private final EventService eventService;

    @GetMapping
    @PreAuthorize("isEventAdmin(#event)")
    public String index(Model model, @Required Event event, Pageable pageable) {
        model.addAttribute("orders", orderService.listOrders(event, pageable));
        setActiveAssociation(model, event.getAssociation());
        return "orders/index";
    }

    @GetMapping("/show")
    public String show(Model model, @Required Order order) {
        model.addAttribute("queuePosition", orderService.getQueuePosition(order));
        setActiveAssociation(model, order.getEvent().getAssociation());
        return "orders/show";
    }

    @GetMapping("/todo")
    @PreAuthorize("isEventAdmin(#event)")
    public String todo(Model model, @Required Event event) {
        setActiveAssociation(model, event.getAssociation());
        return "orders/todo";
    }

    @PostMapping("/start")
    @PreAuthorize("isOrderAdmin(#order)")
    public ResponseEntity<String> start(@Required Order order) {
        orderService.start(order);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/reset")
    @PreAuthorize("isOrderAdmin(#order)")
    public ResponseEntity<String> reset(@Required Order order) {
        orderService.reset(order);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/complete")
    @PreAuthorize("isOrderAdmin(#order)")
    public ResponseEntity<String> complete(@Required Order order) {
        orderService.complete(order);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/reject")
    @PreAuthorize("isOrderAdmin(#order)")
    public ResponseEntity<String> reject(@Required Order order) {
        orderService.reject(order);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/create")
    public String createForm(Model model, @Required Table table, OrderForm orderForm) {
        Optional<Event> eventOptional = eventService.getCurrentEvent(table.getLocation());
        if(eventOptional.isEmpty()) {
            model.addAttribute("location", table.getLocation());
            return "events/noEvent";
        }

        Event event = eventOptional.get();

        setActiveAssociation(model, event.getAssociation());
        model.addAttribute("event", event);

        if (!event.isOpen()) {
            return "events/closed";
        }

        List<Item> items = getItems(event);
        model.addAttribute("items", items);
        orderForm.setItems(
                items.stream()
                        .map((item) -> {
                            OrderItemForm orderItem = new OrderItemForm();
                            orderItem.setAmount(0);
                            orderItem.setItemId(item.getId());
                            return orderItem;
                        })
                        .collect(Collectors.toList())
        );
        return "orders/create";
    }

    private List<Item> getItems(Event event) {
        return event.getItems().stream()
                .filter(Item::isAvailable)
                .sorted(Comparator.comparing(Item::getName))
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    public String create(Model model, @Valid OrderForm orderForm, BindingResult bindingResult, @Required Table table) {
        Optional<Event> eventOptional = eventService.getCurrentEvent(table.getLocation());
        if(eventOptional.isEmpty()) {
            model.addAttribute("location", table.getLocation());
            return "events/noEvent";
        }

        Event event = eventOptional.get();

        model.addAttribute("event", event);
        setActiveAssociation(model, event.getAssociation());

        if (!event.isOpen()) {
            return "events/closed";
        }

        if (bindingResult.hasErrors()) {
            List<Item> items = getItems(event);
            model.addAttribute("items", items);
            Map<Long, Integer> current = orderForm.getItems().stream()
                    .collect(Collectors.toMap(
                            OrderItemForm::getItemId,
                            OrderItemForm::getAmount
                    ));
            orderForm.getItems().addAll(
                    items.stream()
                            .map((item) -> {
                                OrderItemForm orderItem = new OrderItemForm();
                                orderItem.setAmount(current.getOrDefault(item.getId(), 0));
                                orderItem.setItemId(item.getId());
                                return orderItem;
                            })
                            .collect(Collectors.toList())
            );
            return "orders/create";
        }

        Order order = formConverter.build(orderForm);
        order.setEvent(event);
        order.getItems().removeIf((item) -> !item.getItem().getEvent().getId().equals(event.getId()));
        order.getItems().removeIf((item) -> !item.getItem().isAvailable());

        order = orderService.create(table, order);
        return redirect(order);
    }

    public static String redirect(Order order) {
        return redirect(order, "show");
    }

    public static String redirect(Order order, String action) {
        return String.format("redirect:/orders/%s?order=%s", action, order.getId());
    }
}
