package be.winagent.weba2.domain.repositories;

import be.winagent.weba2.domain.models.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends CrudRepository<Location, UUID> {
    @NonNull
    List<Location> findAll();
}
