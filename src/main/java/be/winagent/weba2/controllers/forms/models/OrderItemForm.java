package be.winagent.weba2.controllers.forms.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderItemForm {
    @NotNull
    private Long itemId;

    @Min(0)
    @NotNull
    private Integer amount = 0;
}
