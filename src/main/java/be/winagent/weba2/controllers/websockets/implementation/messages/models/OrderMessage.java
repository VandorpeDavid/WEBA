package be.winagent.weba2.controllers.websockets.implementation.messages.models;

import be.winagent.weba2.domain.models.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderMessage {
    private UUID id;
    private int queuePosition;
    private OrderStatus status;
    private String table;
    private ZonedDateTime created;
    private List<OrderItemMessage> items = new ArrayList<>();
}
