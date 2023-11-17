package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.OrderDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.api.exceptions.OrderStatusException;
import by.temniakov.testtask.api.mappers.OrderMapper;
import by.temniakov.testtask.api.services.OrderService;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.OrderRepository;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final ControllerHelper controllerHelper;
    private final OrderRepository orderRepository;
    private OrderService orderService;
    private OrderMapper orderMapper;

    public static final String GET_ORDER = "/orders/{id_order}";
    public static final String FETCH_ORDERS = "/orders";
    public static final String CHANGE_ORDER_STATUS = "/orders/{id_order}/status/change";
    // TODO: 17.11.2023 add Pageable
    public static final String CHANGE_ORDER = "/orders/{id_order}/status/change";
    public static final String CREATE_ORDER = "/orders";
    public static final String DELETE_ORDER = "/orders/{id_order}";

    @PatchMapping(CHANGE_ORDER_STATUS)
    public ResponseEntity<OrderDto> changeOrderStatus(
            @PathVariable(name = "id_order") Integer orderId,
            @ValueOfEnum(enumClass = Status.class) @RequestParam(name = "new_status")
            String newStatus
    ){
        Orders order = controllerHelper.getOrderOrThrowException(orderId);
        orderService.changeOrderStatus(order, Status.valueOf(newStatus));

        return ResponseEntity.of(Optional.of(order).map(orderMapper::toDto));
    }


    @DeleteMapping(DELETE_ORDER)
    public ResponseEntity<OrderDto> deleteOrder(
            @PathVariable(name="id_order") Integer orderId){
        Orders order = controllerHelper.getOrderOrThrowException(orderId);
        if (order.getStatus() == Status.ACTIVE) {
            throw new InUseException(
                    "Can't remove an active order.", orderId);
        }

        orderRepository.delete(order);
        return ResponseEntity.ok().build();
    }
}
