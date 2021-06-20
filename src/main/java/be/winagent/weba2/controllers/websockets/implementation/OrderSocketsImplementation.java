package be.winagent.weba2.controllers.websockets.implementation;

import be.winagent.weba2.controllers.websockets.OrderSockets;
import be.winagent.weba2.controllers.websockets.implementation.messages.converter.OrderMessageConverter;
import be.winagent.weba2.controllers.websockets.implementation.messages.models.OrderMessage;
import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Order;
import be.winagent.weba2.services.EventService;
import be.winagent.weba2.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class OrderSocketsImplementation implements OrderSockets {
    private final SimpMessagingTemplate messagingTemplate;
    private final OrderMessageConverter orderMessageConverter;
    private final OrderService orderService;
    private final EventService eventService;
    private static final String ORDER_CHANNEL = "/orders/";
    private static final String EVENT_ORDER_CHANNEL = "/events/unfinishedOrders/";

    public void publish(Order order) {
        publish(order, false);
    }

    @Override
    public void publish(Order order, boolean updateQueue) {
        publishToDedicatedChannel(order); // Update to customer
        broadcast(order); // Update admin panel
        if (updateQueue) {
            // TODO: debounce
            updateQueuePositions(order.getEvent()); // Update orders from other customers
        }
    }

    public void updateQueuePositions(Event event) {
        List<Order> orders = orderService.listUnfinishedOrders(event);
        for (int i = 0; i < orders.size(); ++i) {
            OrderMessage message = orderMessageConverter.build(orders.get(i));
            message.setQueuePosition(i);
            publishToDedicatedChannel(message);
        }
    }

    private void publishToDedicatedChannel(Order order) {
        OrderMessage orderMessage = orderMessageConverter.build(order);
        orderMessage.setQueuePosition(orderService.getQueuePosition(order));

        publishToDedicatedChannel(orderMessage);
    }

    private void publishToDedicatedChannel(OrderMessage orderMessage) {
        messagingTemplate.convertAndSend(
                String.format("%s%s", ORDER_CHANNEL, orderMessage.getId()),
                orderMessage
        );
    }

    private void broadcast(Order order) {
        OrderMessage orderMessage = orderMessageConverter.build(order);
        broadcast(order.getEvent(), orderMessage);
    }

    private void broadcast(Event event, OrderMessage orderMessage) {
        messagingTemplate.convertAndSend(
                String.format("%s%s", EVENT_ORDER_CHANNEL, event.getId()),
                orderMessage
        );
    }

    private void broadcastAllUnfinished(Event event) {
        List<Order> orders = orderService.listUnfinishedOrders(event);
        for (int i = 0; i < orders.size(); ++i) {
            OrderMessage message = orderMessageConverter.build(orders.get(i));
            message.setQueuePosition(i);
            broadcast(event, message);
        }
    }


    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        GenericMessage message = (GenericMessage) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");

        if (simpDestination != null) {
            if (simpDestination.startsWith(ORDER_CHANNEL)) {
                String uuid = simpDestination.substring(ORDER_CHANNEL.length());
                orderService.find(UUID.fromString(uuid))
                        .ifPresent(this::publishToDedicatedChannel);
            } else if (simpDestination.startsWith(EVENT_ORDER_CHANNEL)) {
                String uuid = simpDestination.substring(EVENT_ORDER_CHANNEL.length());
                eventService.find(UUID.fromString(uuid))
                        .ifPresent(this::broadcastAllUnfinished);
            }
        }
    }
}
