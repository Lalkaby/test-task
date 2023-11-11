package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    private BigDecimal price;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "good",
            targetEntity = GoodOrderEntity.class,fetch = FetchType.LAZY)
    private List<GoodOrderEntity> orderAssoc = new ArrayList<>();
}

