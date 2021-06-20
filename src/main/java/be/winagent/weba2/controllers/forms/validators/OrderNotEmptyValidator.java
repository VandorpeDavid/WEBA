package be.winagent.weba2.controllers.forms.validators;

import be.winagent.weba2.controllers.forms.models.OrderForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderNotEmptyValidator implements ConstraintValidator<OrderNotEmpty, OrderForm> {
    @Override
    public boolean isValid(OrderForm orderForm, ConstraintValidatorContext constraintValidatorContext) {
        return orderForm.getItems().stream().anyMatch((order) -> order.getAmount() > 0);
    }
}
