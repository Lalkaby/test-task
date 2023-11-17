package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodDto {
    public static final GoodDto EMPTY = new GoodDto();

    @Null(message = "must be null", groups = {CreationInfo.class, IdNullInfo.class})
    private Integer id;

    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank(message = "must be null or not blank", groups = UpdateInfo.class)
    private String title;

    @Min(value = 0, message = "amount must be not less than 0")
    @Max(value = Integer.MAX_VALUE)
    private Integer amount ;

    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank(message = "must be null or not blank", groups = UpdateInfo.class)
    private String producer;

    @NotNull(message = "must be not null",groups = CreationInfo.class)
    @ValueOfEnum(enumClass = Currency.class, groups = {CreationInfo.class, UpdateInfo.class})
    private String currency;

    @DecimalMin(value = "0", message = "price must be more than 0", inclusive = false)
    @DecimalMax(value = "100", message = "price must be less than 100", inclusive = false)
    private BigDecimal price;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "number_orders")
    @Null(message = "must be null")
    private Integer numberOrders;
}
