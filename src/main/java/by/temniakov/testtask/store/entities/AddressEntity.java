package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.enums.City;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tsk_address")
public class AddressEntity {

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
            targetEntity = OrderEntity.class )
    private List<OrderEntity> orders = new ArrayList<>();
}
