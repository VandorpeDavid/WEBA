package be.winagent.weba2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationConfig {
    private String baseUrl;
}
