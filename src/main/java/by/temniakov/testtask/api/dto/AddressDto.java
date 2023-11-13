package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    public static final AddressDto EMPTY = new AddressDto();

    private Integer id;

    private City city;

    @NullOrNotBlank
    private String street;

    @NullOrNotBlank
    private String house;
}
