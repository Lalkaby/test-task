package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.GoodOrderDto;
import by.temniakov.testtask.api.exceptions.UpdateOrderStatusException;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.entities.GoodOrder;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.GoodRepository;
import by.temniakov.testtask.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final GoodRepository goodRepository;

    public void changeOrderStatus(Orders order, Status newStatus){
        OrderStatusChanger.changeStatus(order, newStatus);
        switch (newStatus) {
            case DRAFT, COMPLETED -> {
            }
            case ACTIVE -> {
                updateOrderTime(order);
                withdrawGoods(order);
            }
            case CANCELLED -> {
                restoreGoods(order);
            }
        }
        orderRepository.saveAndFlush(order);
    }

    private void updateOrderTime(Orders order) {
        order.setOrderTime(Instant.now());
    }

    // TODO: 20.11.2023 saveAndFlush order?
    private void withdrawGoods(Orders order) {
        for (var goodAssoc : order.getGoodAssoc()){
            Good good = goodAssoc.getGood();
            Integer withdrawAmount = goodAssoc.getAmount();
            goodAssoc.getGood().setAmount(good.getAmount() - withdrawAmount);
            goodRepository.saveAndFlush(good);
        }
    }

    // TODO: 20.11.2023 check if works, saveAndFlush?, catch exceptions when id-s not in goods
    public void addGoods(Orders order, List<GoodOrderDto> goodOrdersDto) {
        if (goodOrdersDto == null || goodOrdersDto.isEmpty()) return;
        List<Good> goods = goodRepository
                .findAllById(
                        goodOrdersDto
                                .stream()
                                .map(GoodOrderDto::getGoodId)
                                .toList());

        Map<Good,Integer> goodAmountMap = goods
                .stream()
                .collect(Collectors.toMap(good -> good,Good::getAmount));

        order.getGoodAssoc().addAll(
                goodAmountMap.entrySet()
                        .stream()
                        .map(entry -> new GoodOrder(entry.getKey(),order,entry.getValue()))
                        .toList());
    }

    // TODO: 20.11.2023 saveAndFlush order?
    private void restoreGoods(Orders order) {
        for (var goodAssoc : order.getGoodAssoc()){
            Good good = goodAssoc.getGood();
            Integer restoredAmount = goodAssoc.getAmount();
            goodAssoc.getGood().setAmount(good.getAmount() + restoredAmount);
            goodRepository.saveAndFlush(good);
        }
    }

    private static class OrderStatusChanger {
        public static void changeStatus(Orders order, Status newStatus) {
            switch (order.getStatus()){
                case null ->{
                    order.setStatus(newStatus);
                }
                case DRAFT -> {
                    updateDraft(order,newStatus);
                }
                case ACTIVE -> {
                    updateActive(order,newStatus);
                }
                case CANCELLED,COMPLETED  -> {
                    updateToSameOrThrowException(order, newStatus);
                }
            }
        }

        private static void updateActive(Orders order, Status newStatus) {
            switch (newStatus){
                case DRAFT,ACTIVE -> {
                    order.setStatus(newStatus);
                }
                default -> throwIllegalStatusUpdate(order,newStatus);
            }
        }

        private static void updateDraft(Orders order, Status newStatus){
            switch (newStatus){
                case DRAFT,ACTIVE -> {
                    order.setStatus(newStatus);
                }
                default -> throwIllegalStatusUpdate(order,newStatus);
            }
        }

        private static void updateToSameOrThrowException(Orders order, Status newStatus){
            if (!order.getStatus().equals(newStatus)){
                throwIllegalStatusUpdate(order,newStatus);
            }
        }

        private static void throwIllegalStatusUpdate(Orders order, Status newStatus){
            throw new UpdateOrderStatusException(
                    "Illegal status update.", order.getId(), order.getStatus(), newStatus);
        }
    }
}
