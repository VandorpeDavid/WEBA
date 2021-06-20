package be.winagent.weba2.domain.repositories;

import be.winagent.weba2.domain.models.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    @NonNull
    List<Item> findAllById(@NonNull Iterable<Long> ids);
}
