package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsk_order")
public class OrderEntity {
    @Id
    private Integer id;

    @ManyToOne(targetEntity = AddressEntity.class)
    @JoinColumn(name = "id_address",referencedColumnName = "id")
    private AddressEntity address;

    @Column
    private String username;

    @Builder.Default
    @Column(name = "order_time",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant orderTime = Instant.now();


    @Column(name = "phone_number")
    private String phoneNumber;

    @NullOrNotBlank
    @Pattern(message = "Email is not valid",
            regexp = RegexpConstants.EMAIL)
    @Column(name = "user_email")
    private String userEmail;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "order",
            targetEntity = GoodOrderEntity.class)
    private List<GoodOrderEntity> goodAssoc = new ArrayList<>();
}

