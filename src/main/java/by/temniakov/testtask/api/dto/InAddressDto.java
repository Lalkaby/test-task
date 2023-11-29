package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Address object for sending to server")
public class InAddressDto {
    @Schema(description = "City from address for delivery",
            example = "VITEBSK",
            implementation = City.class,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "must be not null",groups = CreationInfo.class)
    @ValueOfEnum(enumClass = City.class,groups = {CreationInfo.class, UpdateInfo.class})
    private String city;

    @Schema(description = "Street from address for delivery",
            example = "Moscow avenue",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NullOrNotBlank(groups = UpdateInfo.class)
    @NotBlank(message = "must contains at least one non-whitespace character",
            groups = CreationInfo.class)
    private String street;

    @Schema(description = "Full house number from address for delivery",
            example = "42/4",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NullOrNotBlank(groups= UpdateInfo.class)
    @NotBlank(message = "must contains at least one non-whitespace character",
            groups = CreationInfo.class)
    private String house;
}
