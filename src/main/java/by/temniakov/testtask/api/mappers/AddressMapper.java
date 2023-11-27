package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.InAddressDto;
import by.temniakov.testtask.api.dto.OutAddressDto;
import by.temniakov.testtask.store.entities.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {BaseMapper.class})
public interface AddressMapper{
    OutAddressDto toOutDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Address fromDto(InAddressDto addressDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(InAddressDto addressDTO, @MappingTarget Address address);


    @Mapping(target = "orders", ignore = true)
    Address clone(Address address);
}
