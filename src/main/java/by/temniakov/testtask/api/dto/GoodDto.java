package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodDto {
    public static final GoodDto EMPTY = new GoodDto();

    private Integer id;

    @NullOrNotBlank
    private String title;

    @Min(value = 0, message = "Amount must be not less than 0")
    @Max(value = Integer.MAX_VALUE)
    private Integer amount;

    @NullOrNotBlank
    private String producer;

    @Null
    private Currency currency;

    @DecimalMin(value = "0", message = "Price must be more than 0", inclusive = false)
    @DecimalMax(value = "100", message = "Price must be less than 100", inclusive = false)
    private BigDecimal price;

    @JsonProperty(value = "number_orders")
    @Null
    private Integer numberOrders;
}
