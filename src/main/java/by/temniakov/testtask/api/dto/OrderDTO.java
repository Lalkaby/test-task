package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.store.enums.RegexpConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    public static final OrderDTO EMPTY = new OrderDTO();

    @NotNull
    private Integer id;

    @NotBlank
    private String username;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "order_time")
    private Instant orderTime;

    @NotBlank
    @Email(regexp = RegexpConstants.EMAIL)
    private String email;
    
    @NotNull
    private AddressDTO address;

    // TODO: 10.11.2023 Think about it
    @NotNull
    private List<GoodDTO> goods;

    @Null
    private Integer amount;
}
