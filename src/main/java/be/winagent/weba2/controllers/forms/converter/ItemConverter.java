package be.winagent.weba2.controllers.forms.converter;

import be.winagent.weba2.controllers.forms.models.ItemForm;
import be.winagent.weba2.domain.models.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ItemConverter implements BidirectionalConverter<Item, ItemForm> {
    @Override
    public Item update(Item target, ItemForm source) {
        target.setName(source.getName());
        target.setPrice(source.getPrice());
        target.setPhoto(source.getPhoto());
        target.setAvailable(source.isAvailable());
        return target;
    }

    @Override
    public Item build(ItemForm source) {
        return update(new Item(), source);
    }

    @Override
    public ItemForm reverseUpdate(ItemForm target, Item source) {
        target.setName(source.getName());
        target.setAvailable(source.isAvailable());
        target.setPrice(source.getPrice());
        target.setPhoto(source.getPhoto());
        return target;
    }

    @Override
    public ItemForm reverseBuild(Item source) {
        return reverseUpdate(new ItemForm(), source);
    }
}
