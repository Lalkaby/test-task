package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.enums.City;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private City city;

    @Column
    private String street;

    @Column
    private String house;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "address",
            targetEntity = Orders.class )
    private List<Orders> orders = new ArrayList<>();
}
