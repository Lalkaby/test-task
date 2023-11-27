package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.dto.InGoodOrderDto;
import by.temniakov.testtask.api.dto.InOrderDto;
import by.temniakov.testtask.api.dto.OutOrderDto;
import by.temniakov.testtask.api.services.GoodOrderService;
import by.temniakov.testtask.api.services.OrderService;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final GoodOrderService goodOrderService;

    public static final String GET_ORDER = "/orders/{id_order}";
    public static final String FETCH_ORDERS = "/orders";
    public static final String CHANGE_ORDER_STATUS = "/orders/{id_order}/status/change";
    public static final String ADD_ORDER_GOODS = "/orders/{id_order}/goods";
    public static final String DELETE_ORDER_GOODS = "/orders/{id_order}/goods/{id_good}";
    public static final String CREATE_ORDER = "/orders";
    public static final String UPDATE_ORDER = "/orders/{id_order}";
    public static final String DELETE_ORDER = "/orders/{id_order}";

    @GetMapping(GET_ORDER)
    public ResponseEntity<OutOrderDto> getOrderById(
            @PathVariable(name = "id_order") Integer orderId) {

        return ResponseEntity.of(Optional.of(orderService.getDtoByIdOrThrowException(orderId)));
    }

    @GetMapping(FETCH_ORDERS)
    public ResponseEntity<List<OutOrderDto>> fetchFilteredSortedOrders(
            @RequestParam(name = "phone_number",defaultValue = "",required = false) String phoneNumber,
            Pageable pageable) {

        return ResponseEntity.of(
                Optional.of(orderService.findFilteredAndSortedDtoByPageable(phoneNumber,pageable))
        );
    }

    @PostMapping(CREATE_ORDER)
    public ResponseEntity<OutOrderDto> createOrder(
            @RequestBody InOrderDto createOrderDto) {
        Orders order = orderService.createOrder(createOrderDto);
        goodOrderService.addGoods(order, createOrderDto.getGoodOrders());
        orderService.refresh(order);

        OutOrderDto orderDto = orderService.getDtoFromOrder(order);
        return ResponseEntity.of(Optional.of(orderDto));
    }

    @PatchMapping(ADD_ORDER_GOODS)
    public ResponseEntity<OutOrderDto> addOrderGoods(
            @PathVariable(name = "id_order") Integer orderId,
            @RequestBody List<InGoodOrderDto> goodOrdersDto){
        goodOrderService.addGoods(orderId, goodOrdersDto);

        OutOrderDto orderDto = orderService.getDtoByIdOrThrowException(orderId);
        return ResponseEntity.of(Optional.of(orderDto));
    }

    @PatchMapping(CHANGE_ORDER_STATUS)
    public ResponseEntity<OutOrderDto> changeOrderStatus(
            @PathVariable(name = "id_order") Integer orderId,
            @RequestParam(name = "new_status") String newStatus){
        Orders order = orderService.changeOrderStatus(orderId, newStatus);

        return ResponseEntity.of(Optional.of(orderService.getDtoFromOrder(order)));
    }

    @PatchMapping(value = UPDATE_ORDER)
    public ResponseEntity<OutOrderDto> updateOrderGood(
            @PathVariable(name = "id_order") Integer orderId,
            @RequestBody InOrderDto orderDto){
        OutOrderDto updatedOrderDto = orderService
                .getDtoFromOrder(orderService.getUpdatedOrder(orderId, orderDto));

        return ResponseEntity.of(
                Optional.of(updatedOrderDto));
    }

    @DeleteMapping(DELETE_ORDER)
    public ResponseEntity<InOrderDto> deleteOrder(
            @PathVariable(name="id_order") Integer orderId) {
        orderService.delete(orderId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(DELETE_ORDER_GOODS)
    public ResponseEntity<OutOrderDto> deleteOrderGoods(
            @PathVariable(name = "id_order") Integer orderId,
            @PathVariable(name = "id_good") Integer goodId) {
        goodOrderService.deleteGood(orderId,goodId);

        return ResponseEntity.of(Optional.of(orderService.getDtoByIdOrThrowException(orderId)));
    }
}
