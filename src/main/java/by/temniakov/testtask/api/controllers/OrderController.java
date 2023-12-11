package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.dto.InGoodOrderDto;
import by.temniakov.testtask.api.dto.InOrderDto;
import by.temniakov.testtask.api.dto.OutOrderDto;
import by.temniakov.testtask.api.services.GoodOrderService;
import by.temniakov.testtask.api.services.OrderService;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Orders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name="order", description = "Order management APIs")
public class OrderController {
    private final OrderService orderService;
    private final GoodOrderService goodOrderService;

    @GetMapping("{id}")
    @Operation(
            tags = {"get","order"},
            summary = "Retrieve order by Id",
            description = "Get a Order object by specifying its id")
    public ResponseEntity<OutOrderDto> getOrderById(
            @Parameter(description = "Id of retrieving order", example = "1")
            @PathVariable(name = "id") Integer id) {

        return ResponseEntity.of(Optional.of(orderService.getDtoByIdOrThrowException(id)));
    }

    @GetMapping
    @Operation(
            tags = {"get","order"},
            summary = "Retrieve sorted orders by page and size and filtered by phone number",
            description = "Fetch of orders objects with sorting, pagination and filtering by phone number")
    public ResponseEntity<List<OutOrderDto>> fetchFilteredSortedOrders(
            @Parameter(description = "Substring for filtering by phone number", example = "+375")
            @RequestParam(name = "phone_number",defaultValue = "",required = false)
            String phoneNumber,
            Pageable pageable) {

        return ResponseEntity.of(
                Optional.of(orderService.findFilteredAndSortedDtoByPageable(phoneNumber,pageable))
        );
    }

    @PostMapping
    @Operation(
            tags = {"post","order"},
            summary = "Create a new order",
            description = "Create a new order from order object ")
    public ResponseEntity<OutOrderDto> createOrder(
            @RequestBody InOrderDto createOrderDto) {
        Orders order = orderService.createOrder(createOrderDto);
        goodOrderService.addGoods(order, createOrderDto.getGoodOrders());
        orderService.refresh(order);

        OutOrderDto orderDto = orderService.getDtoFromOrder(order);
        return ResponseEntity.of(Optional.of(orderDto));
    }

    @PatchMapping("/{id}/goods")
    @Operation(
            tags = {"patch","order"},
            summary = "Add goods to order by Id",
            description = "Add goods to order by id and array of Good Order object")
    public ResponseEntity<OutOrderDto> addOrderGoods(
            @Parameter(description = "Order id, where the goods are added", example = "1")
            @PathVariable(name = "id") Integer id,
            @RequestBody List<InGoodOrderDto> goodOrdersDto){
        goodOrderService.addGoods(id, goodOrdersDto);

        OutOrderDto orderDto = orderService.getDtoByIdOrThrowException(id);
        return ResponseEntity.of(Optional.of(orderDto));
    }

    @PatchMapping("/{id}/status/change")
    @Operation(
            tags = {"patch","order"},
            summary = "Change order delivery status",
            description = "Change order delivery status")
    public ResponseEntity<OutOrderDto> changeOrderStatus(
            @Parameter(description = "Order id which status will be changed")
            @PathVariable(name = "id") Integer id,
            @Parameter(
                    description = "New order status",
                    example = "ACTIVE",
                    schema = @Schema(implementation = Status.class))
            @RequestParam(name = "new_status") String newStatus){
        Orders order = orderService.changeOrderStatus(id, newStatus);

        return ResponseEntity.of(Optional.of(orderService.getDtoFromOrder(order)));
    }

    @PatchMapping(value = "{id}")
    @Operation(
            tags = {"patch","order"},
            summary = "Update order by Id",
            description = "Update order by its id and order object")
    public ResponseEntity<OutOrderDto> updateOrder(
            @Parameter(description = "Update existing order by id", example = "1")
            @PathVariable(name = "id") Integer id,
            @Schema(implementation = InOrderDto.class)
            @RequestBody InOrderDto orderDto){
        OutOrderDto updatedOrderDto = orderService
                .getDtoFromOrder(orderService.getUpdatedOrder(id, orderDto));

        return ResponseEntity.of(
                Optional.of(updatedOrderDto));
    }

    @DeleteMapping("{id}")
    @Operation(
            tags = {"delete","order"},
            summary = "Remove order by Id",
            description = "Delete order by its id")
    public ResponseEntity<InOrderDto> deleteOrder(
            @Parameter(description = "Order id to be deleted", example = "1")
            @PathVariable(name="id") Integer id) {
        orderService.delete(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id_order}/goods/{id_good}")
    @Operation(
            tags = {"delete","order"},
            summary = "Remove good by Id from order by Id",
            description = "Delete good from order if order is drafting")
    public ResponseEntity<OutOrderDto> deleteOrderGood(
            @Parameter(description = "Order id from which the good will be removed", example = "1")
            @PathVariable(name = "id_order") Integer orderId,
            @Parameter(description = "Good id which will be removed", example = "1")
            @PathVariable(name = "id_good") Integer goodId) {
        goodOrderService.deleteGood(orderId,goodId);

        return ResponseEntity.of(Optional.of(orderService.getDtoByIdOrThrowException(orderId)));
    }
}
