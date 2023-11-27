package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class InOrderDto {
    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank( groups = UpdateInfo.class)
    private String username;

    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank( groups = UpdateInfo.class)
    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank( groups = UpdateInfo.class)
    //@Email(regexp = RegexpConstants.EMAIL, message = "email must be valid")
    @JsonProperty(value = "user_email")
    private String userEmail;

    @NotNull(message = "must be not null",groups = CreationInfo.class)
    @JsonProperty(value = "id_address")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer addressId;

    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "order_goods")
    private List<InGoodOrderDto> goodOrders;

    @Null(message = "must be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer amount;

    @Null(message = "must be null")
    private BigDecimal price;
}
