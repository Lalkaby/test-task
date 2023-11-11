package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.OrderDTO;
import by.temniakov.testtask.store.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.control.DeepClone;

@Mapper(componentModel = "spring")
public interface OrderMapper extends BaseMapper{
    @Mapping(source = "id", target = "id")
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().mapToInt(GoodOrderEntity::getAmount).sum())",
            target = "amount")
    OrderDTO toDTO(OrderEntity entity);

    @Mapping(source = "id", target = "id", ignore = true)
    void updateEntityFromDTO(OrderDTO addressDTO, @MappingTarget OrderEntity entity);

    @DeepClone
    OrderEntity clone(OrderEntity entity);
}
