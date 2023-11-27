package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutGoodDto {
    private Integer id;

    private String title;

    private Integer amount;

    private String producer;

    @ValueOfEnum(enumClass = Currency.class)
    private String currency;

    private BigDecimal price;

    @JsonProperty(value = "number_orders")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberOrders;
}
