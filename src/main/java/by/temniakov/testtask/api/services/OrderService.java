package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.GoodOrderDto;
import by.temniakov.testtask.api.exceptions.*;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.entities.GoodOrder;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.GoodOrderRepository;
import by.temniakov.testtask.store.repositories.GoodRepository;
import by.temniakov.testtask.store.repositories.OrderRepository;
import by.temniakov.testtask.store.repositories.OrderRepositoryCustomImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class OrderService {
    private final OrderRepository orderRepository;
    private final GoodOrderRepository goodOrderRepository;
    private final GoodRepository goodRepository;
    private final OrderRepositoryCustomImpl orderRepositoryCustom;

    public void delete(Orders order){
        if (order.getStatus() == Status.ACTIVE) {
            throw new OrderStatusException(
                    "Can't remove an active order.", order.getId(), order.getStatus());
        }
        orderRepository.delete(order);
    }

    public void changeOrderStatus(Orders order, Status newStatus){
        OrderStatusChanger.changeStatus(order, newStatus);
        switch (newStatus) {
            case DRAFT, COMPLETED -> {
            }
            case ACTIVE -> {
                tryWithdrawGoods(order);
                updateOrderTime(order);
            }
            case CANCELLED -> restoreGoods(order);
        }
        orderRepository.saveAndFlush(order);
    }

    @Transactional
    public void addGoods(Orders order, @Valid List<GoodOrderDto> goodOrdersDto) {
        if (!order.getStatus().equals(Status.DRAFT)){
            throw new OrderStatusException("Can't update not the draft order.", order.getId(), order.getStatus());
        }
        if (goodOrdersDto == null || goodOrdersDto.isEmpty()) return;
        List<Good> goods = getGoods(goodOrdersDto);

        List<Integer> goodIds = goods.stream().map(Good::getId).toList();

        Map<Integer,Integer> goodIdWithdrawAmountMap = goodOrdersDto
                .stream()
                .filter(Objects::nonNull)
                .filter(goodOrderDto -> goodIds.contains(goodOrderDto.getGoodId()))
                .collect(Collectors.toMap(GoodOrderDto::getGoodId, GoodOrderDto::getAmount));

        List<GoodOrder> newGoodOrders = goods
                .stream()
                .map(good -> new GoodOrder(good, order, goodIdWithdrawAmountMap.get(good.getId())))
                .toList();

//        goodOrderRepository.saveAllAndFlush(newGoodOrders);
//        orderRepositoryCustom.refresh(order);
    }

    public Page<Orders> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Orders saveAndFlush(Orders order) {
        return orderRepository.saveAndFlush(order);
    }

    public Orders save(Orders order) {
        return orderRepository.save(order);
    }

    public Optional<Orders> findById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    public Page<Orders> findAll(Example<Orders> example, Pageable pageable){
        return orderRepository.findAll(example,pageable);
    }

    @Transactional
    public void deleteGood(Integer orderId, Integer goodId) {
        GoodOrder goodOrder = goodOrderRepository
                .findGoodOrderByOrder_IdAndGood_Id(orderId,goodId)
                .orElseThrow(()-> new NotFoundException("No such good in the order", goodId));

        goodOrder.setOrder(Hibernate.unproxy(goodOrder.getOrder(), Orders.class));

        if (!goodOrder.getOrder().getStatus().equals(Status.DRAFT)){
            throw new OrderStatusException(
                    "Can't update not the draft order.",
                    goodOrder.getOrder().getId(),
                    goodOrder.getOrder().getStatus());
        }

        goodOrderRepository.delete(goodOrder);
    }


    private void updateOrderTime(Orders order) {
        order.setOrderTime(Instant.now());
    }

    private void withdrawGoods(Orders order) {
        for (var goodAssoc : order.getGoodAssoc()){
            Good good = goodAssoc.getGood();
            Integer withdrawAmount = goodAssoc.getAmount();
            goodAssoc.getGood().setAmount(good.getAmount() - withdrawAmount);
        }

        goodRepository.saveAllAndFlush(
                order.getGoodAssoc()
                        .stream()
                        .map(GoodOrder::getGood)
                        .toList());
    }


    private void tryWithdrawGoods(Orders order) {
        List<GoodOrder> notValidAmounts = order.getGoodAssoc()
                .stream()
                .filter(goodOrder ->
                        goodOrder.getAmount() > goodOrder.getGood().getAmount())
                .toList();

        if (!notValidAmounts.isEmpty()) {
            List<InvalidGoodOrder> invalidGoods = notValidAmounts.stream()
                    .map(goodOrder ->
                            new InvalidGoodOrder(
                                    goodOrder.getGood().getId(),
                                    goodOrder.getAmount(),
                                    goodOrder.getGood().getAmount()))
                    .toList();
            throw new InvalidOrderAmountException("Invalid amount detected.",order.getId(), invalidGoods);
        }

        withdrawGoods(order);
    }

    private List<Good> getGoods(List<GoodOrderDto> goodOrdersDto) {
        List<Integer> goodIds =  goodOrdersDto
                .stream()
                .filter(Objects::nonNull)
                .map(GoodOrderDto::getGoodId)
                .filter(Objects::nonNull)
                .toList();
        List<Good> resultGoods = new ArrayList<>();

        if (!goodIds.isEmpty()) {
            resultGoods = goodRepository.findAllById(goodIds);
        }

        if (resultGoods.isEmpty()) {
            throw new NotFoundException("No goods were found.",goodIds);
        }

        return resultGoods;
    }

    private void restoreGoods(Orders order) {
        for (var goodAssoc : order.getGoodAssoc()){
            Good good = goodAssoc.getGood();
            Integer restoredAmount = goodAssoc.getAmount();
            goodAssoc.getGood().setAmount(good.getAmount() + restoredAmount);
        }

        goodRepository.saveAllAndFlush(
                order.getGoodAssoc()
                        .stream()
                        .map(GoodOrder::getGood)
                        .toList());
    }

    private static class OrderStatusChanger {
        public static void changeStatus(Orders order, Status newStatus) {
            switch (order.getStatus()){
                case null -> order.setStatus(newStatus);
                case DRAFT -> updateDraft(order,newStatus);
                case ACTIVE -> updateActive(order,newStatus);
                case CANCELLED,COMPLETED  -> updateToSameOrThrowException(order, newStatus);
            }
        }

        private static void updateActive(Orders order, Status newStatus) {
            switch (newStatus){
                case ACTIVE,CANCELLED,COMPLETED -> order.setStatus(newStatus);
                default -> throwIllegalStatusUpdate(order,newStatus);
            }
        }

        private static void updateDraft(Orders order, Status newStatus){
            switch (newStatus){
                case DRAFT -> order.setStatus(newStatus);
                case ACTIVE -> {
                    if (order.getGoodAssoc().isEmpty()) throw new EmptyOrderException("Order goods must not be empty", order.getId());
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
