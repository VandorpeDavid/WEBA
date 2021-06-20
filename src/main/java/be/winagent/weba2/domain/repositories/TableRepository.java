package be.winagent.weba2.domain.repositories;

import be.winagent.weba2.domain.models.Table;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TableRepository extends CrudRepository<Table, UUID> {
}
