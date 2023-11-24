package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
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
public class OrderDto {
    public static final OrderDto EMPTY = new OrderDto();

    @Null(message = "must be null", groups = {CreationInfo.class, IdNullInfo.class})
    private Integer id;

    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank(message = "must be null or not blank", groups = UpdateInfo.class)
    private String username;

    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank(message = "must be null or not blank", groups = UpdateInfo.class)
    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @Null(message = "must be null")
    @JsonProperty(value = "order_time")
    private Instant orderTime;

    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @NullOrNotBlank(message = "must be null or not blank", groups = UpdateInfo.class)
    @Email(regexp = RegexpConstants.EMAIL, message = "email must be valid")
    @JsonProperty(value = "user_email")
    private String userEmail;

    @Null(message = "must be null")
    private AddressDto address;

    @NotNull(message = "must be not null",groups = CreationInfo.class)
    @JsonProperty(value = "id_address")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer addressId;

    @Null(message = "must be null")
    private String status ;

    @Null(message = "must be null")
    private List<GoodDto> goods;

    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "order_goods")
    private List<GoodOrderDto> goodOrders;

    @Null(message = "must be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer amount;

    @Null(message = "must be null")
    private BigDecimal price;
}
