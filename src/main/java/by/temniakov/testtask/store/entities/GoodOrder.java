package by.temniakov.testtask.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GoodOrderId.class)
public class GoodOrder {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_good", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Good good;

    @Id
    @ManyToOne(targetEntity = Orders.class)
    @JoinColumn(name = "id_order", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Orders order;

    @Column
    @Min(value = 0, message = "Amount cannot be less then 0")
    private Integer amount;

}
