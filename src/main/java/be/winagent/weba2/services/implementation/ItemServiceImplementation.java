package be.winagent.weba2.services.implementation;

import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Item;
import be.winagent.weba2.domain.repositories.ItemRepository;
import be.winagent.weba2.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ItemServiceImplementation implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Optional<Item> find(long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Item create(Event event, Item item) {
        item.setEvent(event);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findAllByIds(Collection<Long> ids) {
        return itemRepository.findAllById(ids);
    }
}
