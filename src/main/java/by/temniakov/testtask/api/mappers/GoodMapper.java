package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.InGoodDto;
import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {BaseMapper.class},imports = {Status.class})
public interface GoodMapper{
    @Mapping(expression =
            "java(entity.getOrderAssoc().stream()" +
                    ".filter(x->x.getOrder().getStatus().equals(Status.COMPLETED)).toList().size())",
            target = "numberOrders")
    OutGoodDto toOutDto(Good entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderAssoc",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(InGoodDto dto, @MappingTarget Good entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderAssoc",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Good fromDto(InGoodDto createGoodDto);

    @Mapping(target = "orderAssoc",ignore = true)
    Good clone(Good entity);
}
