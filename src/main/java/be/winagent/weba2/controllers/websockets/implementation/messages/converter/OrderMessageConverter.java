package be.winagent.weba2.controllers.websockets.implementation.messages.converter;

import be.winagent.weba2.controllers.forms.converter.Converter;
import be.winagent.weba2.domain.models.Order;
import be.winagent.weba2.controllers.websockets.implementation.messages.models.OrderItemMessage;
import be.winagent.weba2.controllers.websockets.implementation.messages.models.OrderMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderMessageConverter implements Converter<OrderMessage, Order> {

    @Override
    public OrderMessage update(OrderMessage orderMessage, Order order) {
        orderMessage.setStatus(order.getStatus());
        orderMessage.setId(order.getId());
        orderMessage.setTable(order.getTable().getName());
        orderMessage.setCreated(order.getCreated());
        orderMessage.setItems(
                order.getItems().stream()
                        .map(
                                (orderItem) -> {
                                    OrderItemMessage item = new OrderItemMessage();
                                    item.setAmount(orderItem.getAmount());
                                    item.setPricePerItem(orderItem.getPrice());
                                    item.setName(orderItem.getName());

                                    return item;
                                }
                        )
                        .collect(Collectors.toList())
        );

        return orderMessage;
    }

    @Override
    public OrderMessage build(Order order) {
        return update(new OrderMessage(), order);
    }
}
