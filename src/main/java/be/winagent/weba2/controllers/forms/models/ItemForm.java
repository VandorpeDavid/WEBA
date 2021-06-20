package be.winagent.weba2.controllers.forms.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemForm {
    @NotBlank
    @Length(max = 255)
    private String name;

    @Min(0)
    @NotNull
    private Integer price;

    private boolean available;

    @Length(max = 255)
    @URL(protocol = "https")
    private String photo;
}
