package by.temniakov.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Good in order object for sending to server")
public class InGoodOrderDto {
    @Schema(description = "Good id of existing good",
            example = "69",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "must be not null")
    @JsonProperty(value = "id_good")
    Integer goodId;

    @Schema(description = "Number of goods ordered",
            example = "69",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "must be not less than 1")
    @NotNull(message = "must be not null")
    Integer amount;
}
