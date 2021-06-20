package be.winagent.weba2.controllers.websockets.implementation.messages.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemMessage {
    private int pricePerItem;
    private int amount;
    private String name;
}
