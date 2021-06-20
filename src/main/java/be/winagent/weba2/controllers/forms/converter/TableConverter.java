package be.winagent.weba2.controllers.forms.converter;

import be.winagent.weba2.controllers.forms.models.TableForm;
import be.winagent.weba2.domain.models.Table;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TableConverter implements BidirectionalConverter<Table, TableForm> {
    @Override
    public Table update(Table target, TableForm source) {
        target.setName(source.getName());
        return target;
    }

    @Override
    public Table build(TableForm source) {
        return update(new Table(), source);
    }

    @Override
    public TableForm reverseUpdate(TableForm target, Table source) {
        target.setName(source.getName());
        return target;
    }

    @Override
    public TableForm reverseBuild(Table source) {
        return reverseUpdate(new TableForm(), source);
    }
}
