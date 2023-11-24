package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.GoodDto;
import by.temniakov.testtask.api.dto.GoodOrderDto;
import by.temniakov.testtask.api.dto.OrderDto;
import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.api.exceptions.OrderStatusException;
import by.temniakov.testtask.api.mappers.OrderMapper;
import by.temniakov.testtask.api.mappers.factories.SortOrderFactory;
import by.temniakov.testtask.api.services.OrderService;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.AddressRepository;
import by.temniakov.testtask.store.repositories.OrderRepository;
import by.temniakov.testtask.store.repositories.OrderRepositoryCustomImpl;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final ControllerHelper controllerHelper;
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final SortOrderFactory sortOrderFactory;

    public static final String GET_ORDER = "/orders/{id_order}";
    public static final String FETCH_ORDERS = "/orders";
    public static final String FETCH_SORTED_ORDERS = "/orders/sort";
    public static final String FETCH_FILTERED_ORDERS = "/orders/filter";
    public static final String CHANGE_ORDER_STATUS = "/orders/{id_order}/status/change";
    public static final String ADD_ORDER_GOODS = "/orders/{id_order}/goods";
    public static final String DELETE_ORDER_GOODS = "/orders/{id_order}/goods/{id_good}";
    public static final String CREATE_ORDER = "/orders";

    public static final String UPDATE_ORDER = "/orders/{id_order}";
    public static final String DELETE_ORDER = "/orders/{id_order}";

    @GetMapping(GET_ORDER)
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable(name = "id_order") Integer orderId) {
        Orders order = controllerHelper.getOrderOrThrowException(orderId);

        return ResponseEntity.of(Optional.of(order).map(orderMapper::toDto));
    }

    @GetMapping(FETCH_ORDERS)
    public ResponseEntity<List<OrderDto>> fetchGoods(
           @PageableDefault(page = 0, size = 50) Pageable pageable) {
        return ResponseEntity.of(
                Optional.of(
                        orderService
                                .findAll(pageable)
                                .map(orderMapper::toDto)
                                .toList()
                )
        );
    }

    @GetMapping(FETCH_SORTED_ORDERS)
    public ResponseEntity<List<OrderDto>> fetchSortedOrders(
            @PageableDefault(page = 0, size = 50, sort = "id",direction = Sort.Direction.ASC)
            Pageable pageable) {
        Sort newSort = Sort.by(pageable.getSort()
                .filter(order -> sortOrderFactory.getFilterKeys().contains(order.getProperty()))
                .map(sortOrderFactory::fromJsonSortOrder)
                .toList());

        PageRequest newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);

        return ResponseEntity.of(
                Optional.of(
                        orderService
                                .findAll(newPage)
                                .map(orderMapper::toDto)
                                .toList()
                )
        );
    }

    @GetMapping(FETCH_FILTERED_ORDERS)
    public ResponseEntity<List<OrderDto>> fetchFilteredGoods(
            @RequestParam(name = "phone_number",defaultValue = "") String phoneNumber,
            @PageableDefault(page = 0, size = 50) Pageable pageable){
        Orders filterOrder = Orders.builder()
                .phoneNumber(phoneNumber.trim())
                .orderTime(null)
                .build();

        Example<Orders> filterExample = Example.of(
                filterOrder,
                ExampleMatcher
                        .matching()
                        .withStringMatcher(
                                ExampleMatcher.StringMatcher.CONTAINING));

        return ResponseEntity.of(
                Optional.of(
                        orderService
                                .findAll(filterExample,pageable)
                                .map(orderMapper::toDto)
                                .toList()
                )
        );
    }

    @PostMapping(CREATE_ORDER)
    public ResponseEntity<OrderDto> createOrder(
            @Validated(value = {CreationInfo.class, Default.class})
            @RequestBody OrderDto orderDto) {
        Orders order = orderMapper.fromDto(orderDto);

        order = orderService.save(order);
        orderService.addGoods(order, orderDto.getGoodOrders());
        orderService.refresh(order);

        return ResponseEntity.of(Optional.of(order).map(orderMapper::toDto));
    }

    @PatchMapping(ADD_ORDER_GOODS)
    public ResponseEntity<OrderDto> addOrderGoods(
            @PathVariable(name = "id_order") Integer orderId,
            @RequestBody List<GoodOrderDto> goodOrdersDto){
        Orders order = controllerHelper.getOrderOrThrowException(orderId);
        orderService.addGoods(order, goodOrdersDto);
        orderService.refresh(order);

        return ResponseEntity.of(Optional.of(order).map(orderMapper::toDto));
    }

    @PatchMapping(CHANGE_ORDER_STATUS)
    public ResponseEntity<OrderDto> changeOrderStatus(
            @PathVariable(name = "id_order") Integer orderId,
            @ValueOfEnum(enumClass = Status.class) @RequestParam(name = "new_status")
            String newStatus){
        Orders order = controllerHelper.getOrderOrThrowException(orderId);
        orderService.changeOrderStatus(order, Status.valueOf(newStatus));

        return ResponseEntity.of(Optional.of(order).map(orderMapper::toDto));
    }

    @PatchMapping(value = UPDATE_ORDER)
    public ResponseEntity<OrderDto> updateOrderGood(
            @PathVariable(name = "id_order") Integer orderId,
            @Validated(value = {UpdateInfo.class, IdNullInfo.class, Default.class})
            @RequestBody OrderDto orderDto){
        Orders order = controllerHelper.getOrderOrThrowException(orderId);

        orderMapper.updateFromDto(orderDto, order);
        Orders savedOrder = orderService.saveAndFlush(order);

        return ResponseEntity.of(Optional.of(savedOrder).map(orderMapper::toDto));
    }

    @DeleteMapping(DELETE_ORDER)
    public ResponseEntity<OrderDto> deleteOrder(
            @PathVariable(name="id_order") Integer orderId) {
        orderService.delete(orderId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(DELETE_ORDER_GOODS)
    public ResponseEntity<OrderDto> deleteOrderGoods(
            @PathVariable(name = "id_order") Integer orderId,
            @PathVariable(name = "id_good") Integer goodId) {
        orderService.deleteGood(orderId,goodId);
        Orders order = controllerHelper.getOrderOrThrowException(orderId);

        return ResponseEntity.of(Optional.of(order).map(orderMapper::toDto));
    }
}
