package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    public static final AddressDto EMPTY = new AddressDto();

    private Integer id;

    @ValueOfEnum(enumClass = City.class, nullable = true)
    private String city;

    @NullOrNotBlank
    private String street;

    @NullOrNotBlank
    private String house;
}
