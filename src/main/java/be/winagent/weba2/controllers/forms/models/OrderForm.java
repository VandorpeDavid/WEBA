package be.winagent.weba2.controllers.forms.models;

import be.winagent.weba2.controllers.forms.validators.OrderNotEmpty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@OrderNotEmpty
public class OrderForm {
    @NotEmpty
    @NotNull
    private List<OrderItemForm> items = new ArrayList<>();
}
