package be.winagent.weba2.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
@Getter
public class TimezoneConfiguration {
    private final ZoneId zoneId = ZoneId.of("Europe/Brussels");
}
