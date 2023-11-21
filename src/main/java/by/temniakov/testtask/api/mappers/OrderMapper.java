package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.OrderDto;
import by.temniakov.testtask.api.mappers.factories.SortOrderFactory;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.AddressRepository;
import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import java.math.BigDecimal;

import by.temniakov.testtask.store.entities.GoodOrder;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

// TODO: 11.11.2023 Finish mapping
@Mapper(componentModel = "spring",
        uses = {GoodOrderMapper.class, AddressMapper.class, BaseMapper.class, GoodOrderMapper.class},
        imports =  BigDecimal.class)
public interface OrderMapper{
    GoodOrderMapper goodOrderMapper = Mappers.getMapper(GoodOrderMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "username",target = "username")
    @Mapping(source = "userEmail",target = "userEmail")
    @Mapping(source = "phoneNumber",target = "phoneNumber")
    @Mapping(source = "orderTime",target = "orderTime")
    @Mapping(target = "goodOrders",ignore = true)
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().mapToInt(GoodOrder::getAmount).sum())",
            target = "amount")
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().map(goodOrderMapper::toGoodDto).toList())"
            ,target = "goods")
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().map(x->x.getGood().getPrice().multiply(BigDecimal.valueOf(x.getAmount()))).reduce(BigDecimal.ZERO,BigDecimal::add))"
            ,target = "price")
    OrderDto toDto(Orders entity);

    @Mapping(target = "goodAssoc",ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "address", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(OrderDto addressDTO, @MappingTarget Orders entity);

    @DeepClone
    Orders clone(Orders entity);

    @Mapping(target = "goodAssoc", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "address")
    Orders fromDto(OrderDto orderDto);
}
