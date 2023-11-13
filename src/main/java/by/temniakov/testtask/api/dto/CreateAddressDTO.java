package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.CityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.temniakov.testtask.enums.City.MINSK;
import static by.temniakov.testtask.enums.City.VITEBSK;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressDTO {
    @CityEnum(anyOf = {VITEBSK,MINSK})
    private City city;

    @NotBlank(message = "must contains at least one non-whitespace character")
    private String street;

    @NotBlank(message = "must contains at least one non-whitespace character")
    private String house;
}
