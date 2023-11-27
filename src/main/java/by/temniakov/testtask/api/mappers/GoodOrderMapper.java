package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.store.entities.GoodOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {BaseMapper.class})
public interface GoodOrderMapper {

    @Mapping(expression = "java(goodOrder.getGood().getId())", target = "id")
    @Mapping(expression = "java(goodOrder.getGood().getTitle())", target = "title")
    @Mapping(source = "amount", target = "amount")
    @Mapping(expression = "java(goodOrder.getGood().getProducer())", target = "producer")
    @Mapping(expression = "java(goodOrder.getGood().getPrice())", target = "price")
    @Mapping(source = "goodOrder.good.currency", target = "currency")
    @Mapping(target = "numberOrders",ignore = true)
    OutGoodDto toOutGoodDto(GoodOrder goodOrder);
}
