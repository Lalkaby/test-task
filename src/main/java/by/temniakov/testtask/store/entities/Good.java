package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Transient
    private Integer numberOrders;

    @Builder.Default
    @OneToMany(mappedBy = "good",
            targetEntity = GoodOrder.class,fetch = FetchType.LAZY)
    @BatchSize(size = 25)
    private List<GoodOrder> orderAssoc = new ArrayList<>();
}

