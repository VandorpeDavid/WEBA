package be.winagent.weba2.security;

import com.kakawait.spring.boot.security.cas.autoconfigure.CasHttpSecurityConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * Application specific Spring Security config, overwriting Spring Boot Security : http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-security
 * Below are some examples to configure the {@link HttpSecurity} , more information here : https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-httpsecurity
 * <p>
 * This is also configuration for Refund to act as a Service Provider in SAML
 * Using the Spring Boot SAML plugin : https://github.com/ulisesbocchio/spring-boot-security-saml
 *
 * @author lbdwerch
 * @see WebSecurityConfigurerAdapter
 * @since v0.0.1
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http


               // .logout()
               // .logoutUrl("/logout")
              //  .and()
                .authorizeRequests()
                .antMatchers("/elections/results", "/elections/cantvote", "/elections/vote/token")
                .permitAll();

        CasHttpSecurityConfigurer.cas().configure(http);
    }
}
