package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.CityEnum;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
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
public class AddressDTO {
    public static final AddressDTO EMPTY = new AddressDTO();

    private Integer id;

    @CityEnum(anyOf = {VITEBSK,MINSK})
    private City city;

    @NullOrNotBlank
    private String street;

    @NullOrNotBlank
    private String house;
}
