package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.InOrderDto;
import by.temniakov.testtask.api.dto.OutOrderDto;
import by.temniakov.testtask.api.exceptions.*;
import by.temniakov.testtask.api.mappers.OrderMapper;
import by.temniakov.testtask.api.mappers.factories.SortOrderFactory;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.entities.GoodOrder;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.OrderRepository;
import by.temniakov.testtask.store.repositories.OrderRepositoryCustomImpl;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderRepositoryCustomImpl orderRepositoryCustom;
    private final SortOrderFactory sortOrderFactory;
    private final OrderMapper orderMapper;
    private final GoodService goodService;


    public void delete(Integer orderId){
        checkExistsAndThrowException(orderId);

        checkStatusForRemove(orderId);

        orderRepository.deleteById(orderId);
    }

    private void checkStatusForRemove(Integer orderId) {
        Status orderStatus = getOrderStatusById(orderId);

        if (orderStatus == Status.ACTIVE) {
            throw new OrderStatusException(
                    "Can't remove an active order.", orderId, orderStatus);
        }
    }

    public Status getOrderStatusById(Integer orderId) {
        return orderRepository.getOrderStatusById(orderId);
    }

    public Orders changeOrderStatus(
            Integer orderId, String newStatus){
        Status status = Status.valueOf(newStatus);
        Orders order = getByIdOrThrowException(orderId);
        OrderStatusChanger.changeStatus(order, status);

        switch (status) {
            case DRAFT, COMPLETED -> {
            }
            case ACTIVE -> {
                tryWithdrawGoods(order);
                updateOrderTime(order);
            }
            case CANCELLED -> restoreGoods(order);
        }

        return orderRepository.saveAndFlush(order);
    }

    public List<Orders> findAll(Pageable pageable){
        return orderRepository.findAllBy(pageable);
    }

    public List<Orders> findAllByPhoneNumber(String phoneNumber,Pageable pageable) {
        return orderRepository.findAllByPhoneNumberContaining(phoneNumber, pageable);
    }

    public Orders saveAndFlush(Orders order){return orderRepository.saveAndFlush(order);}

    public Orders save(Orders order){
        return orderRepository.save(order);
    }

    public void refresh(Orders order){
        orderRepositoryCustom.refresh(order);
    }

    public void checkExistsAndThrowException(Integer orderId){
        if (!orderRepository.existsById(orderId)){
            throw new NotFoundException("Order doesn't exists.", orderId);
        }
    }


    private void updateOrderTime(Orders order){
        order.setOrderTime(Instant.now());
    }

    private void withdrawGoods(Orders order){
        for (var goodAssoc : order.getGoodAssoc()){
            Good good = goodAssoc.getGood();
            Integer withdrawAmount = goodAssoc.getAmount();
            goodAssoc.getGood().setAmount(good.getAmount() - withdrawAmount);
        }

        goodService.saveAllAndFlush(
                order.getGoodAssoc()
                        .stream()
                        .map(GoodOrder::getGood)
                        .toList());
    }

    private void tryWithdrawGoods(Orders order){
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

    private void restoreGoods(Orders order){
        for (var goodAssoc : order.getGoodAssoc()){
            Good good = goodAssoc.getGood();
            Integer restoredAmount = goodAssoc.getAmount();
            goodAssoc.getGood().setAmount(good.getAmount() + restoredAmount);
        }

        goodService.saveAllAndFlush(
                order.getGoodAssoc()
                        .stream()
                        .map(GoodOrder::getGood)
                        .toList());
    }

    public List<OutOrderDto> findFilteredAndSortedDtoByPageable(
            String phoneNumber, Pageable pageable){
        Sort newSort = Sort.by(pageable.getSort()
                .filter(order -> sortOrderFactory.getFilterKeys().contains(order.getProperty()))
                .map(sortOrderFactory::fromJsonSortOrder)
                .toList());

        PageRequest newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);
        List<Orders> result;
        if (!phoneNumber.isEmpty()){
            result = findAllByPhoneNumber(phoneNumber,newPage);
        }
        else{
            result = findAll(newPage);
        }

        return result
                .stream()
                .map(orderMapper::toOutDto)
                .toList();
    }

    @Nonnull
    private static Example<Orders> getFilterExample(String phoneNumber){
        Orders filterOrder = Orders.builder()
                .phoneNumber(phoneNumber.trim())
                .orderTime(null)
                .status(null)
                .build();

        return Example.of(
                filterOrder,
                ExampleMatcher
                        .matching()
                        .withStringMatcher(
                                ExampleMatcher.StringMatcher.CONTAINING));
    }

    public OutOrderDto getDtoByIdOrThrowException(Integer orderId){
        return orderMapper
                .toOutDto(getByIdOrThrowException(orderId));
    }

    public Orders getByIdOrThrowException(Integer orderId){
        Orders result = orderRepository
                .findById(orderId)
                .orElseThrow(()->
                        new NotFoundException("Order doesn't exists.", orderId)
                );

        return result;
    }

    private void fillAmountOfGoods(List<Orders> orders) {
        Map<Integer, Integer> numberOrder = getAmountOfGoods(
                orders.stream().map(Orders::getId).toList());
        orders.forEach(good -> good
                .setAmount(numberOrder.get(good.getId())));
    }

    private Map<Integer, Integer> getAmountOfGoods(List<Integer> orderIds){
        return orderRepository
                .getOrdersAmountOfGoods(orderIds)
                .stream()
                .collect(Collectors.toMap(
                        x->x.get(0, Integer.class),
                        x->x.get(1, Long.class).intValue()));
    }

    @Transactional
    public Orders getUpdatedOrder(
            Integer orderId, InOrderDto orderDto){
        Orders order = getByIdOrThrowException(orderId);
        orderMapper.updateFromDto(orderDto, order);

        return order;
    }

    public OutOrderDto getDtoFromOrder(Orders order){
        return orderMapper.toOutDto(order);
    }

    @Transactional
    public Orders createOrder(InOrderDto createOrderDto){
        return save(orderMapper.fromDto(createOrderDto));
    }

    private static class OrderStatusChanger{
        public static void changeStatus(Orders order, Status newStatus){
            switch (order.getStatus()){
                case DRAFT -> updateDraft(order,newStatus);
                case ACTIVE -> updateActive(order,newStatus);
                case CANCELLED,COMPLETED  -> throwIllegalStatusUpdate(order,newStatus);
            }
        }

        private static void updateActive(Orders order, Status newStatus){
            switch (newStatus){
                case CANCELLED,COMPLETED -> order.setStatus(newStatus);
                default -> throwIllegalStatusUpdate(order,newStatus);
            }
        }

        private static void updateDraft(Orders order, Status newStatus){
            switch (newStatus){
                case ACTIVE -> {
                    if (order.getGoodAssoc().isEmpty()) throw new EmptyOrderException("Order goods must not be empty", order.getId());
                    order.setStatus(newStatus);
                }
                default -> throwIllegalStatusUpdate(order,newStatus);
            }
        }

        private static void throwIllegalStatusUpdate(Orders order, Status newStatus){
            throw new UpdateOrderStatusException(
                    "Illegal status update.", order.getId(), order.getStatus(), newStatus);
        }
    }
}
