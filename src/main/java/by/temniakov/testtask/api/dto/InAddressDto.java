package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

// TODO: 24.11.2023 in and out dto
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InAddressDto {
    @NotNull(message = "must be not null",groups = CreationInfo.class)
    @ValueOfEnum(enumClass = City.class,groups = {CreationInfo.class, UpdateInfo.class})
    private String city;

    @NullOrNotBlank(message = "must be null or not blank", groups = UpdateInfo.class)
    @NotBlank(message = "must contains at least one non-whitespace character",
            groups = CreationInfo.class)
    private String street;

    @NullOrNotBlank(message = "must be null or not blank", groups= UpdateInfo.class)
    @NotBlank(message = "must contains at least one non-whitespace character",
            groups = CreationInfo.class)
    private String house;
}
