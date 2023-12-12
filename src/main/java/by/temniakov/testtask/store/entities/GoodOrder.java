package by.temniakov.testtask.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GoodOrderId.class)
public class GoodOrder {
    @Id
    @ManyToOne(targetEntity = Good.class,cascade = {CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "id_good", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Good good;

    @Id
    @ManyToOne(targetEntity = Orders.class,
            cascade = {CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "id_order", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Orders order;

    @Column
    @Min(value = 0, message = "Amount cannot be less then 0")
    private Integer amount;
}
