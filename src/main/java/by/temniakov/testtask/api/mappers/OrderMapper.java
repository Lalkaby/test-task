package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.OrderDto;
import by.temniakov.testtask.api.mappers.factories.SortOrderFactory;
import by.temniakov.testtask.store.entities.Orders;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;

// TODO: 11.11.2023 Finish mapping
@Mapper(componentModel = "spring",uses = SortOrderFactory.class)
public interface OrderMapper extends BaseMapper{
    @Mapping(source = "id", target = "id")
    @Mapping(expression =
            "java(entity.getGoodAssoc().stream().mapToInt(GoodOrder::getAmount).sum())",
            target = "amount")
    @Mapping(source = "address",target = "address")
    public OrderDto toDto(Orders entity);

    @Mapping(source = "id", target = "id", ignore = true)
    public void updateFromDto(OrderDto addressDTO, @MappingTarget Orders entity);

    @DeepClone
    public Orders clone(Orders entity);

    public Sort.Order mapToSortOrder(Sort.Order order);

    Orders fromDto(OrderDto orderDto);
}
