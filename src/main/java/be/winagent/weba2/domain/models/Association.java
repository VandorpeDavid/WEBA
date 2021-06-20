package be.winagent.weba2.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Association {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotEmpty
    @Length(max = 255)
    private String abbreviation;

    @Column(unique = true)
    @NotEmpty
    @Length(max = 255)
    private String name;

    @Length(max = 255)
    private String logoUrl;

    @ManyToMany
    private Set<User> admins = new HashSet<>();

    @OneToMany(mappedBy = "association")
    @OrderBy("created")
    private List<Event> events = new ArrayList<>();

    public String toString() {
        return getAbbreviation();
    }
}
