package be.winagent.weba2.controllers.websockets;

import be.winagent.weba2.domain.models.Order;

public interface OrderSockets {
    public void publish(Order order);
    public void publish(Order order, boolean updateQueue);
}
