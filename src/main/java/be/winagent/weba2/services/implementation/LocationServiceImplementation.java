package be.winagent.weba2.services.implementation;

import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.domain.models.Table;
import be.winagent.weba2.domain.repositories.LocationRepository;
import be.winagent.weba2.domain.repositories.TableRepository;
import be.winagent.weba2.services.LocationService;
import be.winagent.weba2.services.TableService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LocationServiceImplementation implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Optional<Location> find(UUID id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<Location> all() {
        return locationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Location create(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location update(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public void remove(Location location) {

    }
}
