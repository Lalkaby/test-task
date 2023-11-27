package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OutOrderDto {
    private Integer id;

    private String username;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "order_time")
    private Instant orderTime;

    @JsonProperty(value = "user_email")
    private String userEmail;

    private OutAddressDto address;

    @ValueOfEnum(enumClass = Status.class)
    private String status ;

    private List<OutGoodDto> goods;

    private Integer amount;

    private BigDecimal price;
}
