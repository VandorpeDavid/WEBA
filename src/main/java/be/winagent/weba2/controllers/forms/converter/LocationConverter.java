package be.winagent.weba2.controllers.forms.converter;

import be.winagent.weba2.controllers.forms.models.EventForm;
import be.winagent.weba2.controllers.forms.models.LocationForm;
import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Location;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LocationConverter implements BidirectionalConverter<Location, LocationForm> {
    @Override
    public Location update(Location location, LocationForm locationForm) {
        location.setName(locationForm.getName());

        return location;
    }

    @Override
    public Location build(LocationForm locationForm) {
        return update(new Location(), locationForm);
    }

    @Override
    public LocationForm reverseUpdate(LocationForm target, Location source) {
        target.setName(source.getName());
        return target;
    }

    @Override
    public LocationForm reverseBuild(Location source) {
        return reverseUpdate(new LocationForm(), source);
    }
}
