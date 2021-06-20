package be.winagent.weba2.services;

import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Order;
import be.winagent.weba2.domain.models.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Optional<Order> find(UUID id);
    Order create(Table table, Order order);
    Order start(Order order);

    Order reset(Order order);

    Order complete(Order order);
    Order cancel(Order order);
    Page<Order> listOrders(Event event, Pageable pageable);
    List<Order> listUnfinishedOrders(Event event);
    int getQueuePosition(Order order);
}
