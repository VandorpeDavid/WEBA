package be.winagent.weba2.domain.types;

import be.winagent.weba2.domain.repositories.EventRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class TsTzRange {
    private final ZonedDateTime start;
    private final ZonedDateTime end;

    public ZonedDateTime lower() {
        return getStart();
    }

    public ZonedDateTime upper() {
        return getEnd();
    }
}
