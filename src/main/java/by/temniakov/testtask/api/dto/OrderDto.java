package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
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

    @NotNull(message = "must be not null", groups = CreationInfo.class)
    private AddressDto address;

    @NotBlank(message = "must contains at least one non-whitespace character", groups = CreationInfo.class)
    @ValueOfEnum(enumClass = Status.class, groups = {CreationInfo.class, UpdateInfo.class, Default.class})
    private String status;

    // TODO: 10.11.2023 Think about it
    @NotNull
    private List<GoodDto> goods;

    private List<GoodOrderDto> goodOrders;

    @Min(value = 0, message = "amount must be not less than 0")
    @Max(value = Integer.MAX_VALUE, message = "amount must be less than integer max value")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer amount;
}
