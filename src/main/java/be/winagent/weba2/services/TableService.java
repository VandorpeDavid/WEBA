package be.winagent.weba2.services;

import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.domain.models.Table;

import java.util.Optional;
import java.util.UUID;

public interface TableService {
    Optional<Table> find(UUID id);
    Table create(Location location, Table table);
    Table update(Table table);
    void remove(Table table);
}
