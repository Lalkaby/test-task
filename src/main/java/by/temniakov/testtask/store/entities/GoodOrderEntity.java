package by.temniakov.testtask.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsk_good_order")
@IdClass(GoodOrderEntityId.class)
public class GoodOrderEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_good", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private GoodEntity good;

    @Id
    @ManyToOne(targetEntity = OrderEntity.class)
    @JoinColumn(name = "id_order", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrderEntity order;

    @Column
    @Min(value = 0, message = "Amount cannot be less then 0")
    private Integer amount;

}
