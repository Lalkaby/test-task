package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
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
@Schema(description = "Order object for retrieving from server")
public class OutOrderDto {
    @Schema(description = "Received order id",
            example = "69")
    private Integer id;

    @Schema(description = "Received customer username",
            example = "69")
    private String username;

    @Schema(description = "Received customer phone number",
            example = "69")
    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @Schema(description = "Received request order time",
            example = "69")
    @JsonProperty(value = "order_time")
    private Instant orderTime;

    @Schema(description = "Received customer email",
            example = "69")
    @JsonProperty(value = "user_email")
    private String userEmail;

    @Schema(description = "Received address for order delivery",
            implementation = OutAddressDto.class)
    private OutAddressDto address;

    @Schema(description = "Received current order status",
            example = "DRAFT",
            implementation = Status.class)
    @ValueOfEnum(enumClass = Status.class)
    private String status ;

    @Schema(description = "Received array goods in order",
            contains = OutGoodDto.class)
    private List<OutGoodDto> goods;

    @Schema(description = "Received number of goods in order",
            example = "69")
    private Integer amount;

    @Schema(description = "Received price of order",
            example = "69")
    private BigDecimal price;
}
