package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

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
    @OneToMany(mappedBy = "good",
            targetEntity = GoodOrder.class,fetch = FetchType.LAZY)
    private List<GoodOrder> orderAssoc = new ArrayList<>();
}

