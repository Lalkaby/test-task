package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressDto {
    @NotNull(message = "must be not null")
    @ValueOfEnum(enumClass = City.class)
    private String city;

    @NotBlank(message = "must contains at least one non-whitespace character")
    private String street;

    @NotBlank(message = "must contains at least one non-whitespace character")
    private String house;
}