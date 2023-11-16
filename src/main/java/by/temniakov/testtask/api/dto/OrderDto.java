package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
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

    private Integer id;

    @NullOrNotBlank
    private String username;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "order_time")
    private Instant orderTime;

    @NullOrNotBlank
    @Email(regexp = RegexpConstants.EMAIL, message = "email must be valid")
    @JsonProperty(value = "user_email")
    private String userEmail;

    @NotNull
    private AddressDto address;

    @ValueOfEnum(enumClass = Status.class)
    private String status;

    // TODO: 10.11.2023 Think about it
    @NotNull
    private List<GoodDto> goods;

    @Null
    private Integer amount;
}
