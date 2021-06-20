package be.winagent.weba2.util;

import be.winagent.weba2.config.TimezoneConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class ThymeleafHelpers {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private final TimezoneConfiguration timezoneConfiguration;

    @Bean
    public Function<String, String> changeLocaleUrl() {
        return (locale) -> ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam("lang", locale).toUriString();
    }

    @Bean
    public Function<Integer, String> money() {
        return (amount) -> String.format("€ %.2f", amount / 100.0);
    }

    @Bean
    public Function<ZonedDateTime, String> datetime() {
        return (tstz) ->dateTimeFormatter.format(tstz.withZoneSameInstant(timezoneConfiguration.getZoneId()));
    }
}
