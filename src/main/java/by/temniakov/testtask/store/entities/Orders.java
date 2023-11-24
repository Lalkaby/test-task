package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@BatchSize(size = 25)
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Address.class)
    @JoinColumn(name = "id_address",referencedColumnName = "id")
    private Address address;

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
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.valueOf("DRAFT");

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "order",cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.REMOVE},
            targetEntity = GoodOrder.class, fetch = FetchType.LAZY)
    @BatchSize(size = 25)
    private List<GoodOrder> goodAssoc = new ArrayList<>();
}