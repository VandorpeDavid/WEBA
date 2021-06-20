package be.winagent.weba2.controllers.forms.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AssociationForm {
    @NotEmpty
    @Length(max = 255)
    private String abbreviation;

    @NotEmpty
    @Length(max = 255)
    private String name;

    @Length(max = 255)
    @URL(protocol="https")
    private String logoUrl;
}
