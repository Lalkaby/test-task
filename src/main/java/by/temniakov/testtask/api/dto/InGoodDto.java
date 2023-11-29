package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Good object for sending to server")
public class InGoodDto {
    @Schema(description = "Full title of good",
            example = "Gala Apples 2 kg",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank(groups = UpdateInfo.class)
    private String title;

    @Min(value = 0, message = "amount must be not less than 0")
    @Max(value = Integer.MAX_VALUE, message = "amount must be less than integer max value")
    @NotNull(message = "must be not null", groups = CreationInfo.class)
    @Schema(description = "Stock level",
            example = "100",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer amount;

    @Schema(description = "Full producer name of current good",
            example = "Garden Paradise Farm",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank(groups = UpdateInfo.class)
    private String producer;

    @Schema(description = "Price of one unit of the current good",
            example = "99.99",
            implementation = Currency.class,
            minimum = "0",
            maximum = "100",
            exclusiveMinimum = true,
            exclusiveMaximum = true,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "must be not null", groups = CreationInfo.class)
    @DecimalMin(value = "0", message = "price must be more than 0", inclusive = false)
    @DecimalMax(value = "100", message = "price must be less than 100", inclusive = false)
    private BigDecimal price;

    @Schema(description = "Currency of the price of the current good",
            example = "BYN",
            implementation = Currency.class,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "must be not null",groups = CreationInfo.class)
    @ValueOfEnum(enumClass = Currency.class, groups = {CreationInfo.class, UpdateInfo.class})
    private String currency;
}
