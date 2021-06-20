package be.winagent.weba2.controllers.forms.converter;

import be.winagent.weba2.controllers.exceptions.NotFoundException;
import be.winagent.weba2.controllers.forms.models.OrderForm;
import be.winagent.weba2.controllers.forms.models.OrderItemForm;
import be.winagent.weba2.domain.models.Item;
import be.winagent.weba2.domain.models.Order;
import be.winagent.weba2.domain.models.OrderItem;
import be.winagent.weba2.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderFormCreationConverter implements Converter<Order, OrderForm> {
    private final ItemService itemService;

    @Override
    public Order update(Order order, OrderForm orderForm) {
       List<Long> itemIds = orderForm.getItems().stream()
                .map(OrderItemForm::getItemId)
                .collect(Collectors.toList());

       Map<Long, Item> items = itemService.findAllByIds(itemIds)
               .stream()
               .collect(Collectors.toMap(
                       Item::getId,
                       Function.identity()
               ));

       List<OrderItem> orderItems = orderForm.getItems().stream()
               .filter((orderItemForm) -> orderItemForm.getAmount() > 0)
               .map((orderItemForm) -> {
                   OrderItem orderItem = new OrderItem();
                   orderItem.setAmount(orderItemForm.getAmount());
                   Item item = items.get(orderItemForm.getItemId());
                   if(item == null) {
                       throw new NotFoundException();
                   }
                   orderItem.setItem(item);
                   orderItem.setPrice(item.getPrice());
                   orderItem.setName(item.getName());
                   return orderItem;
               })
               .collect(Collectors.toList());

       order.setItems(orderItems);

       return order;
    }

    @Override
    public Order build(OrderForm orderForm) {
        return update(new Order(), orderForm);
    }
}
