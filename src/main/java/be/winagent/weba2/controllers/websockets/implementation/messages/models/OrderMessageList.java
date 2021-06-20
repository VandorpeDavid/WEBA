package be.winagent.weba2.controllers.websockets.implementation.messages.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderMessageList {
    private List<OrderMessage> orders = new ArrayList<>();
}
