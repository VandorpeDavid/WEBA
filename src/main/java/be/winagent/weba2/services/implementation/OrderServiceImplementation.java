package be.winagent.weba2.services.implementation;

import be.winagent.weba2.controllers.websockets.OrderSockets;
import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Order;
import be.winagent.weba2.domain.models.OrderStatus;
import be.winagent.weba2.domain.models.Table;
import be.winagent.weba2.domain.repositories.OrderRepository;
import be.winagent.weba2.services.OrderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderSockets orderSockets;
    private static final List<OrderStatus> unfinishedStatusses = new ArrayList<>() {{
        add(OrderStatus.ORDERED);
        add(OrderStatus.STARTED);
    }};

    public OrderServiceImplementation(OrderRepository orderRepository, @Lazy OrderSockets orderSockets) {
        this.orderRepository = orderRepository;
        this.orderSockets = orderSockets;
    }

    @Override
    public Optional<Order> find(UUID id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order create(Table table, Order order) {
        order.setTable(table);
        order.getItems().forEach((item) -> item.setOrder(order));
        Order newOrder = orderRepository.save(order);
        orderSockets.publish(newOrder);
        return newOrder;
    }

    @Override
    public Order start(Order order) {
        boolean updateQueue = order.getStatus().isComplete();
        order.setStatus(OrderStatus.STARTED);
        return update(order, updateQueue);
    }

    @Override
    public Order reset(Order order) {
        boolean updateQueue = order.getStatus().isComplete();
        order.setStatus(OrderStatus.ORDERED);
        return update(order, updateQueue);
    }

    @Override
    public Order complete(Order order) {
        boolean updateQueue = ! order.getStatus().isComplete();

        order.setStatus(OrderStatus.COMPLETED);
        return update(order, updateQueue);
    }

    @Override
    public Order cancel(Order order) {
        boolean updateQueue = ! order.getStatus().isComplete();
        order.setStatus(OrderStatus.CANCELLED);
        return update(order, updateQueue);
    }

    public Order update(Order order, boolean updateQueue) {
        Order newOrder = orderRepository.save(order);
        orderSockets.publish(newOrder, updateQueue);
        return newOrder;
    }

    @Override
    public Page<Order> listOrders(Event event, Pageable pageable) {
        return orderRepository.findAllByEventIdOrderByCreated(event.getId(), pageable);
    }

    @Override
    public List<Order> listUnfinishedOrders(Event event) {
        List<Order> orders = orderRepository.findAllByEventIdAndStatusIsIn(event.getId(), unfinishedStatusses);
        orders.sort(Comparator.comparing(Order::getCreated));
        return orders;
    }

    @Override
    public int getQueuePosition(Order order) {
        if (!unfinishedStatusses.contains(order.getStatus())) {
            return -1;
        }

        return orderRepository.countAllByEventIdAndStatusIsInAndCreatedIsBefore(
                order.getEvent().getId(),
                unfinishedStatusses,
                order.getCreated()
        );
    }
}
