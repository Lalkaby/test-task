package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodDTO {
    public static final GoodDTO EMPTY = new GoodDTO();

    private Integer id;

    @NullOrNotBlank
    private String title;

    @Min(0)
    private Integer amount;

    @NullOrNotBlank
    private String producer;

    @Min(value = 0,message = "Price must be more than 20")
    private Double price;

    @JsonProperty(value = "number_orders")
    private Integer numberOrders;
}
