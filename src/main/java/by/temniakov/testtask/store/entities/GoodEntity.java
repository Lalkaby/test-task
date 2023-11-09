package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.store.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsk_good")
public class GoodEntity {
    @Id
    private Integer id;

    @Column
    private String title;

    @Column
    private Integer amount;

    @Column
    private String producer;

    @Column
    private Double price;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "good",
            targetEntity = GoodOrderEntity.class)
    private List<GoodOrderEntity> orderAssoc = new ArrayList<>();
}

