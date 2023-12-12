package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.InOrderDto;
import by.temniakov.testtask.api.dto.OutOrderDto;
import by.temniakov.testtask.api.services.AddressService;
import by.temniakov.testtask.store.entities.Orders;
import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Mapper(componentModel = "spring",
        uses = {GoodOrderMapper.class, AddressMapper.class, BaseMapper.class},
        imports =  BigDecimal.class)
public abstract class OrderMapper{
    GoodOrderMapper goodOrderMapper = Mappers.getMapper(GoodOrderMapper.class);
    protected AddressService addressService;
    @Autowired
    void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    // TODO: 12.12.2023 write queries for goodAssoc
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().mapToInt(GoodOrder::getAmount).sum())",
            target = "amount")
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().map(goodOrderMapper::toOutGoodDto).toList())"
            ,target = "goods")
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().map(x->x.getGood().getPrice().multiply(BigDecimal.valueOf(x.getAmount()))).reduce(BigDecimal.ZERO,BigDecimal::add))"
            ,target = "price")
    public abstract OutOrderDto toOutDto(Orders entity);

    @Mapping(target = "goodAssoc",ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "address",
            expression = "java(addressService.getByIdOrThrowException(" +
                    "orderDto.getAddressId()==null?entity.getAddress().getId():orderDto.getAddressId()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateFromDto(InOrderDto orderDto, @MappingTarget Orders entity);

    @DeepClone
    public abstract Orders clone(Orders entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goodAssoc", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "address",
            expression = "java(addressService.getByIdOrThrowException(orderDto.getAddressId()))")
    public abstract Orders fromDto(InOrderDto orderDto);
}
