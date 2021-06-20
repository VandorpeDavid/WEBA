package be.winagent.weba2.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final CustomMethodSecurityExpressionHandler customMethodSecurityExpressionHandler;

    public MethodSecurityConfig(CustomMethodSecurityExpressionHandler customMethodSecurityExpressionHandler) {
        this.customMethodSecurityExpressionHandler = customMethodSecurityExpressionHandler;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return customMethodSecurityExpressionHandler;
    }
}