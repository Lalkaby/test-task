package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order object for sending to server")
public class InOrderDto {
    @Schema(description = "Customer name",
            example = "Temniakov Yan",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank( groups = UpdateInfo.class)
    private String username;

    @Schema(description = "Customer phone number",
            example = "+375295201796",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank( groups = UpdateInfo.class)
    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @Schema(description = "Customer email address",
            example = "temniakovya@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank( groups = UpdateInfo.class)
    @Email(regexp = RegexpConstants.EMAIL, message = "email must be valid")
    @JsonProperty(value = "user_email")
    private String userEmail;

    @Schema(description = "Customer address id for delivery",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "must be not null",groups = CreationInfo.class)
    @JsonProperty(value = "id_address")
    private Integer addressId;

    @Schema(description = "Array of started goods in order",
            minContains = 1,
            contains = OutGoodDto.class,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty(value = "order_goods")
    private List<InGoodOrderDto> goodOrders;
}
