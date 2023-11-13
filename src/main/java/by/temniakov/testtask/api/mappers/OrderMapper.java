package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.OrderDto;
import by.temniakov.testtask.store.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.control.DeepClone;

// TODO: 11.11.2023 Finish mapping  
@Mapper(componentModel = "spring")
public interface OrderMapper extends BaseMapper{
    @Mapping(source = "id", target = "id")
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().mapToInt(GoodOrder::getAmount).sum())",
            target = "amount")
    OrderDto toDto(Order entity);

    @Mapping(source = "id", target = "id", ignore = true)
    void updateFromDto(OrderDto addressDTO, @MappingTarget Order entity);

    @DeepClone
    Order clone(Order entity);
}
