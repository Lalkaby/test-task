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
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.*;

@Service
@Validated
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
            Integer orderId, @ValueOfEnum(enumClass = Status.class)String newStatus){
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


    public Page<Orders> findAll(Pageable pageable){
        return orderRepository.findAll(pageable);
    }

    public Page<Orders> findAll(Example<Orders> example,Pageable pageable) {
        return orderRepository.findAll(example, pageable);
    }

    public Orders saveAndFlush(Orders order){return orderRepository.saveAndFlush(order);
    }

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
            String phoneNumber, @PageableDefault(page = 0, size = 50) Pageable pageable){
        Sort newSort = Sort.by(pageable.getSort()
                .filter(order -> sortOrderFactory.getFilterKeys().contains(order.getProperty()))
                .map(sortOrderFactory::fromJsonSortOrder)
                .toList());

        PageRequest newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);
        Page<Orders> resultPage;
        if (!phoneNumber.isEmpty()){
            Example<Orders> filterExample = getFilterExample(phoneNumber);
            resultPage = findAll(filterExample,newPage);
        }
        else{
            resultPage = findAll(newPage);
        }


        return resultPage
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
        return orderRepository
                .findById(orderId)
                .orElseThrow(()->
                        new NotFoundException("Order doesn't exists.", orderId)
                );
    }

    @Validated(value = {UpdateInfo.class,Default.class})
    public Orders getUpdatedOrder(
            Integer orderId,@Valid InOrderDto orderDto){
        Orders order = getByIdOrThrowException(orderId);
        orderMapper.updateFromDto(orderDto, order);

        return saveAndFlush(order);
    }

    public OutOrderDto getDtoFromOrder(Orders order){
        return orderMapper.toOutDto(order);
    }

    @Validated(value = {CreationInfo.class, Default.class})
    public Orders createOrder(
            @Valid InOrderDto createOrderDto){
        Orders order = orderMapper.fromDto(createOrderDto);
        return saveAndFlush(order);
    }

    private static class OrderStatusChanger{
        public static void changeStatus(Orders order, Status newStatus){
            switch (order.getStatus()){
                case DRAFT -> updateDraft(order,newStatus);
                case ACTIVE -> updateActive(order,newStatus);
                case CANCELLED,COMPLETED  -> updateToSameOrThrowException(order, newStatus);
            }
        }

        private static void updateActive(Orders order, Status newStatus){
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
