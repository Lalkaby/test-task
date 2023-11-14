package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodDto {
    public static final GoodDto EMPTY = new GoodDto();

    private Integer id;

    @NullOrNotBlank(message = "must be null or not blank")
    private String title;

    @Min(value = 0, message = "Amount must be not less than 0")
    @Max(value = Integer.MAX_VALUE)
    private Integer amount;

    @NullOrNotBlank
    private String producer;

    @ValueOfEnum(enumClass = Currency.class, nullable = true)
    private String currency;

    @DecimalMin(value = "0", message = "Price must be more than 0", inclusive = false)
    @DecimalMax(value = "100", message = "Price must be less than 100", inclusive = false)
    private BigDecimal price;

    @JsonProperty(value = "number_orders")
    @Null
    private Integer numberOrders;


    public @Min(value = 0, message = "Amount must be not less than 0") @Max(value = Integer.MAX_VALUE) Integer getAmount() {
        return this.amount;
    }
}
