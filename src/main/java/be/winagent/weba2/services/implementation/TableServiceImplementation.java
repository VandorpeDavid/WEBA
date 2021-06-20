package be.winagent.weba2.services.implementation;

import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.domain.models.Table;
import be.winagent.weba2.domain.repositories.TableRepository;
import be.winagent.weba2.services.TableService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TableServiceImplementation implements TableService {
    private final TableRepository tableRepository;

    @Override
    public Optional<Table> find(UUID id) {
        return tableRepository.findById(id);
    }

    @Override
    public Table create(Location location, Table table) {
        table.setLocation(location);
        return tableRepository.save(table);
    }

    @Override
    public Table update(Table table) {
        return tableRepository.save(table);
    }

    @Override
    public void remove(Table table) {
        tableRepository.delete(table);
    }
}
