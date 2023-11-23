package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.GoodDto;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {BaseMapper.class},imports = {Status.class})
public interface GoodMapper{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "producer", target = "producer")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "currency",target = "currency")
    @Mapping(expression =
            "java(entity.getOrderAssoc().stream()" +
                    ".filter(x->x.getOrder().getStatus().equals(Status.COMPLETED)).toList().size())",
            target = "numberOrders")
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
