package be.winagent.weba2.controllers.forms.converter;

import be.winagent.weba2.controllers.forms.models.AssociationForm;
import be.winagent.weba2.domain.models.Association;
import org.springframework.stereotype.Component;

@Component
public class AssociationConverter implements BidirectionalConverter<Association, AssociationForm> {
    @Override
    public AssociationForm reverseUpdate(AssociationForm target, Association source) {
        target.setAbbreviation(source.getAbbreviation());
        target.setName(source.getName());
        target.setLogoUrl(source.getLogoUrl());
        return target;
    }

    @Override
    public AssociationForm reverseBuild(Association source) {
        return reverseUpdate(new AssociationForm(), source);
    }

    @Override
    public Association update(Association target, AssociationForm source) {
        target.setLogoUrl(source.getLogoUrl());
        return target;
    }

    @Override
    public Association build(AssociationForm associationForm) {
        return update(new Association(), associationForm);
    }
}
