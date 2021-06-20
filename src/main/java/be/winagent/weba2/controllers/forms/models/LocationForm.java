package be.winagent.weba2.controllers.forms.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LocationForm {
    @NotEmpty
    @Length(max = 255)
    private String name;
}
