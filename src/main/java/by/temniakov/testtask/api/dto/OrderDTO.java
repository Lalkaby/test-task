package by.temniakov.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
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
    @Nonnull
    private String username;

    @Nonnull
    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @Nonnull
    @JsonProperty(value = "order_time")
    private Instant orderTime;

    private String email;

    @Nonnull
    private AddressDTO address;

    @Nonnull
    private List<GoodDTO> goods;

    @Nonnull
    private Integer amount;
}
