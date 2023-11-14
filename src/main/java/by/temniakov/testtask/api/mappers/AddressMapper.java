package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.AddressDto;
import by.temniakov.testtask.api.dto.CreateAddressDto;
import by.temniakov.testtask.store.entities.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper extends BaseMapper{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "city",target = "city")
    @Mapping(source = "street",target = "street")
    @Mapping(source = "house",target = "house")
    AddressDto toDto(Address address);

    @Mapping(source = "city",target = "city")
    @Mapping(source = "street",target = "street")
    @Mapping(source = "house",target = "house")
    Address fromDto(CreateAddressDto addressDTO);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "city",target = "city")
    @Mapping(source = "street",target = "street")
    @Mapping(source = "house",target = "house")
    @Mapping(target = "orders", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(AddressDto addressDTO, @MappingTarget Address address);


    @Mapping(target = "orders", ignore = true)
    Address clone(Address address);
}
