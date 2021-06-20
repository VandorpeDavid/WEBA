package be.winagent.weba2.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Entity
@javax.persistence.Table(name="tables")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @NotNull
    private Location location;

    @NotNull
    @Length(max = 255)
    private String name;
}
