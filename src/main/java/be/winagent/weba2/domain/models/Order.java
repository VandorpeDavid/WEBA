package be.winagent.weba2.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@javax.persistence.Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @NotNull
    private Table table;

    @ManyToOne
    @NotNull
    private Event event;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status = OrderStatus.ORDERED;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotEmpty
    private List<OrderItem> items = new ArrayList<>();

    @CreationTimestamp
    private ZonedDateTime created;

    public int getTotal() {
        return items.stream()
                .mapToInt(OrderItem::getSubTotal)
                .sum();
    }
}
