package be.winagent.weba2.domain.models;

import be.winagent.weba2.domain.types.PostgresqlTsTzRangeType;
import be.winagent.weba2.domain.types.TsTzRange;
import com.vladmihalcea.hibernate.type.range.PostgreSQLRangeType;
import com.vladmihalcea.hibernate.type.range.Range;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@TypeDef(
        typeClass = PostgresqlTsTzRangeType.class,
        defaultForType = TsTzRange.class
)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @ManyToOne
    private Association association;

    @NotEmpty
    @Length(max = 255)
    private String name;

    @OneToMany(mappedBy = "event")
    private List<Item> items = new ArrayList<>();

    @CreationTimestamp
    private ZonedDateTime created;

    @NotNull
    @Column(columnDefinition = "tstzrange")
    private TsTzRange timeRange;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    private boolean open;

    public List<Table> getTables() {
        return getLocation().getTables();
    }
}
