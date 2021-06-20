package be.winagent.weba2.services;

import be.winagent.weba2.domain.models.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationService {
    Optional<Location> find(UUID id);
    List<Location> all();
    Location create(Location location);
    Location update(Location location);
    void remove(Location location);
}
