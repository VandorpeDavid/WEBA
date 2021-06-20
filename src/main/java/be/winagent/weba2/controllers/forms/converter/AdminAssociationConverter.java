package be.winagent.weba2.controllers.forms.converter;

import be.winagent.weba2.controllers.forms.models.AssociationForm;
import be.winagent.weba2.domain.models.Association;
import org.springframework.stereotype.Component;

@Component
public class AdminAssociationConverter extends AssociationConverter {
    @Override
    public Association update(Association target, AssociationForm source) {
        target = super.update(target, source);
        target.setAbbreviation(source.getAbbreviation());
        target.setName(source.getName());
        return target;
    }

    @Override
    public Association build(AssociationForm associationForm) {
        return update(new Association(), associationForm);
    }
}
