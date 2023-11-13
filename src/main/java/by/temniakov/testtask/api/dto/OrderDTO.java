package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import static by.temniakov.testtask.enums.Status.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    public static final OrderDTO EMPTY = new OrderDTO();

    private Integer id;

    @NullOrNotBlank
    private String username;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "order_time")
    private Instant orderTime;

    @NullOrNotBlank
    @Email(regexp = RegexpConstants.EMAIL)
    @JsonProperty(value = "user_email")
    private String userEmail;
    
    @NotNull
    private AddressDTO address;

    @StatusEnum(anyOf = {ACTIVE,COMPLETED,CANCELLED},nullable = true)
    private Status status;

    // TODO: 10.11.2023 Think about it
    @NotNull
    private List<GoodDTO> goods;

    @Null
    private Integer amount;
}
