package be.winagent.weba2.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @NotNull
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Item item;

    @NotBlank
    @Length(max = 255)
    private String name;

    @Min(0)
    private int price;

    @Positive
    private int amount;

    public int getSubTotal() {
        return amount * item.getPrice();
    }
}
