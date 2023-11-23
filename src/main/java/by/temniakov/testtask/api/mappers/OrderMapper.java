package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.OrderDto;
import by.temniakov.testtask.store.entities.Orders;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import java.math.BigDecimal;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: 11.11.2023 Finish mapping
@Mapper(componentModel = "spring",
        uses = {GoodOrderMapper.class, AddressMapper.class, BaseMapper.class,
                ControllerHelper.class},
        imports =  BigDecimal.class)
public abstract class OrderMapper{
    GoodOrderMapper goodOrderMapper = Mappers.getMapper(GoodOrderMapper.class);
    protected ControllerHelper controllerHelper;
    @Autowired
    void setControllerHelper(ControllerHelper controllerHelper) {
        this.controllerHelper = controllerHelper;
    }

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
    @Mapping(target = "addressId",ignore = true)
    public abstract OrderDto toDto(Orders entity);

    @Mapping(target = "goodAssoc",ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "address", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateFromDto(OrderDto addressDTO, @MappingTarget Orders entity);

    @DeepClone
    public abstract Orders clone(Orders entity);

    @Mapping(target = "goodAssoc", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "address",
            expression = "java(controllerHelper.getAddressOrThrowException(orderDto.getAddressId()))")
    public abstract Orders fromDto(OrderDto orderDto);
}
