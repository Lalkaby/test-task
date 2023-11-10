package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.GoodDTO;
import by.temniakov.testtask.api.helpers.OptionalUtils;
import by.temniakov.testtask.store.entities.GoodEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = OptionalUtils.class)
public interface GoodMapper extends BaseMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "producer", target = "producer")
    @Mapping(source = "price", target = "price")
    @Mapping(expression = "java(entity.getOrderAssoc().size())", target = "numberOrders")
    GoodDTO toDTO(GoodEntity entity);

    @Mapping(source = "id", target = "id",ignore = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "amount", target = "amount",ignore = true)
    @Mapping(source = "producer", target = "producer")
    @Mapping(source = "price", target = "price")
    @Mapping(target = "orderAssoc", ignore = true )
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(GoodDTO goodDTO, @MappingTarget GoodEntity entity);


}
