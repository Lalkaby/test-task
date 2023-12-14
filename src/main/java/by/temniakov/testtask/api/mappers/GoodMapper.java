package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.InGoodDto;
import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.api.services.AddressService;
import by.temniakov.testtask.api.services.GoodService;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.repositories.GoodRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {BaseMapper.class},imports = {Status.class})
public abstract class GoodMapper{
    protected GoodRepository goodRepository;
    @Autowired
    void setAddressService(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

//    @Mapping(expression =
//            "java(entity.getOrderAssoc().stream()" +
//                    ".filter(x->x.getOrder().getStatus().equals(Status.COMPLETED)).toList().size())",
//            target = "numberOrders")
    public abstract OutGoodDto toOutDto(Good entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderAssoc",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateFromDto(InGoodDto dto, @MappingTarget Good entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderAssoc",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract  Good fromDto(InGoodDto createGoodDto);

    @Mapping(target = "orderAssoc",ignore = true)
    public abstract  Good clone(Good entity);
}
