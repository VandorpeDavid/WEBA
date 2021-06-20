package be.winagent.weba2.security;

import be.winagent.weba2.domain.models.*;
import be.winagent.weba2.services.AuthenticationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.util.Optional;

@Getter
@Setter
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private final AuthenticationService authenticationService;

    public CustomMethodSecurityExpressionRoot(Authentication authentication, AuthenticationService authenticationService) {
        super(authentication);
        this.authenticationService = authenticationService;
    }

    public boolean isEventAdmin(Event event) {
        return isAssociationAdmin(event.getAssociation());
    }

    public boolean isOrderAdmin(Order order) {
        return isEventAdmin(order.getEvent());
    }

    public boolean isItemAdmin(Item item) {
        return isEventAdmin(item.getEvent());
    }

    public boolean isAdmin() {
        return getUser()
                .filter(User::isAdmin)
                .isPresent();
    }

    public boolean isAssociationAdmin(Association association) {
        return isAdmin()
                ||
                getUser()
                        .filter(association.getAdmins()::contains)
                        .isPresent();
    }

    private Optional<User> getUser() {
        return authenticationService.getUser(this.getAuthentication());
    }


    // Copied from org.springframework.security.access.expression.method
    private Object filterObject;
    private Object returnObject;
    private Object target;

    void setThis(Object target) {
        this.target = target;
    }

    public Object getThis() {
        return target;
    }
}