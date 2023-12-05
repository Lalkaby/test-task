package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Good object for retrieving from server")
public class OutGoodDto {
    @Schema(description = "Received good id",
            example = "69")
    private Integer id;

    @Schema(description = "Received good title",
            example = "69")
    private String title;

    @Schema(description = "Received stock level or number of goods ordered",
            example = "69")
    private Integer amount;

    @Schema(description = "Received good producer",
            example = "Golder Final Fields Farm")
    private String producer;

    @Schema(description = "Received good price",
            example = "99.99")
    private BigDecimal price;

    @Schema(description = "Received good currency",
            example = "BYN",
            implementation = Currency.class)
    @ValueOfEnum(enumClass = Currency.class)
    private String currency;

    @Schema(description = "Number of current orders",
            nullable = true,
            example = "69")
    @JsonProperty(value = "number_orders")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberOrders;
}
