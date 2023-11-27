package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutAddressDto {
    private Integer id;

    @ValueOfEnum(enumClass = City.class)
    private String city;

    private String street;

    private String house;
}
