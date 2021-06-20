package be.winagent.weba2.controllers.forms.converter;

import be.winagent.weba2.config.TimezoneConfiguration;
import be.winagent.weba2.controllers.exceptions.NotFoundException;
import be.winagent.weba2.controllers.forms.models.EventForm;
import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.services.LocationService;
import com.vladmihalcea.hibernate.type.range.Range;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Getter
public class EventConverter implements BidirectionalConverter<Event, EventForm> {
    @Autowired
    private TimezoneConfiguration timezoneConfiguration;

    @Override
    public Event update(Event event, EventForm eventForm) {
        event.setName(eventForm.getName());
        event.setOpen(eventForm.isOpen());

        return event;
    }

    @Override
    public Event build(EventForm eventForm) {
        return update(new Event(), eventForm);
    }

    @Override
    public EventForm reverseUpdate(EventForm target, Event source) {
        target.setName(source.getName());
        target.setOpen(source.isOpen());

        LocalDateTime start = source.getTimeRange().lower().withZoneSameInstant(timezoneConfiguration.getZoneId()).toLocalDateTime();
        target.setStartDate(start.toLocalDate().toString());
        target.setStartTime(start.toLocalTime());

        LocalDateTime end = source.getTimeRange().upper().withZoneSameInstant(timezoneConfiguration.getZoneId()).toLocalDateTime();
        target.setEndDate(end.toLocalDate().toString());
        target.setEndTime(end.toLocalTime());

        target.setLocationId(source.getLocation().getId());
        return target;
    }

    @Override
    public EventForm reverseBuild(Event source) {
        return reverseUpdate(new EventForm(), source);
    }
}
