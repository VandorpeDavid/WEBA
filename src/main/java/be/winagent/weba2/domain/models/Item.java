package be.winagent.weba2.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Event event;

    @NotBlank
    @Length(max = 255)
    private String name;

    private boolean available;

    @Min(0)
    private int price;

    @Length(max = 255)
    private String photo;
}
