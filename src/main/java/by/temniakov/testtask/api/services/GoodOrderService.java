package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.InGoodOrderDto;
import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.api.exceptions.OrderStatusException;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.entities.GoodOrder;
import by.temniakov.testtask.store.entities.GoodOrderId;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.GoodOrderRepository;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Validated
@RequiredArgsConstructor
public class GoodOrderService {
    private final GoodOrderRepository goodOrderRepository;
    private final OrderService orderService;
    private final GoodService goodService;

    public void checkExistsAndThrowException(GoodOrderId goodOrderId){
        if (!goodOrderRepository.existsById(goodOrderId)){
            throw new NotFoundException("No such good in the order", goodOrderId.getGood());
        }
    }

    @Transactional
    public void deleteGood(Integer orderId, Integer goodId){
        GoodOrderId goodOrderId = new GoodOrderId(goodId,orderId);

        delete(goodOrderId);
    }

    public void deleteById(GoodOrderId goodOrderId){
        goodOrderRepository.deleteById(goodOrderId);
    }

    @Transactional
    @Validated(value = Default.class)
    public void addGoods(Orders order, List<@Valid InGoodOrderDto> goodOrdersDto){
        Status status = order.getStatus();
        if (!status.equals(Status.DRAFT)){
            throw new OrderStatusException("Can't update not the draft order.", order.getId(), status);
        }

        if (goodOrdersDto == null || goodOrdersDto.isEmpty()) return;
        List<Good> goods = tryGetGoods(goodOrdersDto);

        List<Integer> goodIds = goods.stream().map(Good::getId).toList();

        Map<Integer,Integer> goodIdWithdrawAmountMap = goodOrdersDto
                .stream()
                .filter(Objects::nonNull)
                .filter(goodOrderDto -> goodIds.contains(goodOrderDto.getGoodId()))
                .collect(Collectors.toMap(InGoodOrderDto::getGoodId, InGoodOrderDto::getAmount,Integer::sum));

        List<GoodOrder> newGoodOrders = goods
                .stream()
                .map(good -> new GoodOrder(good, order, goodIdWithdrawAmountMap.get(good.getId())))
                .toList();

        saveAll(newGoodOrders);
    }

    private List<Good> tryGetGoods(List<InGoodOrderDto> goodOrdersDto) {
        List<Integer> goodIds = new ArrayList<>(goodOrdersDto
                .stream()
                .filter(Objects::nonNull)
                .map(InGoodOrderDto::getGoodId)
                .filter(Objects::nonNull)
                .distinct()
                .toList());

        List<Integer> existingIds = goodService.getExistingIds(goodIds);
        if (existingIds.size()!=goodIds.size()){
            goodIds.removeAll(existingIds);
            throw new NotFoundException("No such goods", goodIds);
        }

        return getGoods(goodIds);
    }

    @Transactional
    public void addGoods(Integer orderId, List< InGoodOrderDto> goodOrdersDto){
        Orders order = orderService.getByIdOrThrowException(orderId);
        addGoods(order, goodOrdersDto);
    }

    public void delete(GoodOrderId goodOrderId){
        checkExistsAndThrowException(goodOrderId);

        checkOrderStatusForRemove(goodOrderId);

        deleteById(goodOrderId);
    }

    private List<Good> getGoods(List<Integer> goodIds){
        List<Good> resultGoods = goodService.findAllById(goodIds);

        if (resultGoods.isEmpty()) {
            throw new NotFoundException("No goods were found.",goodIds);
        }

        return resultGoods;
    }

    private void checkOrderStatusForRemove(GoodOrderId goodOrderId){
        Status status = orderService.getOrderStatusById(goodOrderId.getOrder());
        if (!status.equals(Status.DRAFT)){
            throw new OrderStatusException(
                    "Can't update not the draft order.",
                    goodOrderId.getOrder(),
                    status);
        }
    }

    public void saveAll(Iterable<GoodOrder> entities){
        goodOrderRepository.saveAll(entities);
    }
}
