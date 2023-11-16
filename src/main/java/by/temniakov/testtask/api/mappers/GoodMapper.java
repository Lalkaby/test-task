package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.AddressDto;
import by.temniakov.testtask.api.dto.GoodDto;
import by.temniakov.testtask.store.entities.Good;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GoodMapper extends BaseMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "producer", target = "producer")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "currency",target = "currency")
    @Mapping(expression = "java(entity.getOrderAssoc().size())", target = "numberOrders")
    GoodDto toDto(Good entity);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "producer", target = "producer")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "currency", target = "currency")
    @Mapping(target = "orderAssoc",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(GoodDto dto, @MappingTarget Good entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "producer", target = "producer")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "currency",target = "currency")
    @Mapping(target = "orderAssoc",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Good fromDto(GoodDto createGoodDto);

    @Mapping(target = "orderAssoc",ignore = true)
    Good clone(Good entity);
}
