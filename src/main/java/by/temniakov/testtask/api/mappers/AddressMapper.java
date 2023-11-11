package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.AddressDTO;
import by.temniakov.testtask.api.dto.GoodDTO;
import by.temniakov.testtask.store.entities.AddressEntity;
import by.temniakov.testtask.store.entities.GoodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.control.DeepClone;

@Mapper(componentModel = "spring")
public interface AddressMapper extends BaseMapper{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "city",target = "city")
    @Mapping(source = "street",target = "street")
    @Mapping(source = "house",target = "house")
    AddressDTO toDTO(AddressEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "city",target = "city")
    @Mapping(source = "street",target = "street")
    @Mapping(source = "house",target = "house")
    @Mapping(target = "orders", ignore = true)
    void updateEntityFromDTO(AddressDTO addressDTO, @MappingTarget AddressEntity entity);


    @Mapping(target = "orders", ignore = true)
    AddressEntity cloneEntity(AddressEntity entity);
}
