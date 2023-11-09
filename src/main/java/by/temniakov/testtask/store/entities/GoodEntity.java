package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.store.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tsk_good")
public class GoodEntity {
    @Id
    private Integer id;

    @Column
    private String title;

    @Column
    @Min(value = 0)
    private Integer amount;

    @Column
    private String producer;

    @Column
    @Min(value = 0)
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

