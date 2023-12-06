package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.api.dto.OutOrderDto;
import by.temniakov.testtask.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@BatchSize(size = 25)
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "order_json")
    @JdbcTypeCode(SqlTypes.JSON)
    private OutOrderDto orderJson;
}
