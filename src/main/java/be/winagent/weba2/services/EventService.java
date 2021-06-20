package be.winagent.weba2.services;

import be.winagent.weba2.domain.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Optional<Event> find(UUID id);
    Event create(Event event);
    Event update(Event event);

    ByteArrayInputStream generateTablePdf(Event event) throws IOException;
    Optional<Event> getCurrentEvent(Location location);

    Page<Event> getFutureEvents(Pageable pageable, Location location);
    Page<Event> getPastEvents(Pageable pageable, Location location);
}
