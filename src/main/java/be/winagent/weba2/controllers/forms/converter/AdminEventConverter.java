package be.winagent.weba2.controllers.forms.converter;

import be.winagent.weba2.controllers.exceptions.NotFoundException;
import be.winagent.weba2.controllers.forms.models.EventForm;
import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.domain.types.TsTzRange;
import be.winagent.weba2.services.LocationService;
import com.vladmihalcea.hibernate.type.range.Range;
import liquibase.pro.packaged.T;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@AllArgsConstructor
public class AdminEventConverter extends EventConverter {
    private final LocationService locationService;

    @Override
    public Event update(Event event, EventForm eventForm) {
        event = super.update(event, eventForm);
        event.setTimeRange(
                new TsTzRange(eventForm.getStartTime().atDate(LocalDate.parse(eventForm.getStartDate())).atZone(getTimezoneConfiguration().getZoneId()),
                        eventForm.getEndTime().atDate(LocalDate.parse(eventForm.getEndDate())).atZone(getTimezoneConfiguration().getZoneId()))
        );

        // This would be a security risk if this weren't admin only. Don't blindly copy this pattern.
        Location location = locationService.find(eventForm.getLocationId()).orElseThrow(NotFoundException::new);
        event.setLocation(location);

        return event;
    }
}
