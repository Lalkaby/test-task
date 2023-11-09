package by.temniakov.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodDTO {
    @Nonnull
    private String title;

    @Nonnull
    private Integer amount;

    @Nonnull
    private String producer;

    @Nonnull
    private Double price;

    @JsonProperty(value = "number_orders")
    private Integer numberOrders;
}
