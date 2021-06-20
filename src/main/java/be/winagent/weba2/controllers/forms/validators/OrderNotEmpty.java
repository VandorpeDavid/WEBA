package be.winagent.weba2.controllers.forms.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OrderNotEmptyValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderNotEmpty {
    String message() default "Order cannot be empty.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
