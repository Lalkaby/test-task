package by.temniakov.testtask.api.dto;

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
public class AddressDTO {
    public static final AddressDTO EMPTY = new AddressDTO();

    @NotNull
    private Integer id;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String house;
}
