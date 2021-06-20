package be.winagent.weba2.services;

import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> find(long id);
    Item create(Event event, Item item);
    Item update(Item item);
    List<Item> findAllByIds(Collection<Long> ids);
}
