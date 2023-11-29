package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Address object for retrieving from server")
public class OutAddressDto {
    @Schema(description = "Received address id",
            example = "69")
    private Integer id;

    @Schema(description = "Received address city",
            example = "VITEBSK",
            implementation = City.class)
    @ValueOfEnum(enumClass = City.class)
    private String city;

    @Schema(description = "Received address street name",
            example = "Moscow avenue")
    private String street;

    @Schema(description = "Received address house name",
            example = "42/4")
    private String house;
}
