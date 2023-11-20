package by.temniakov.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodOrderDto {
    @JsonProperty(value = "id_good")
    Integer goodId;

    @Min(value = 1, message = "must be not less than 1")
    Integer amount;
}
