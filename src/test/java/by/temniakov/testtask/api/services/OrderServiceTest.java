package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.exceptions.EmptyOrderException;
import by.temniakov.testtask.api.exceptions.UpdateOrderStatusException;
import by.temniakov.testtask.api.mappers.OrderMapper;
import by.temniakov.testtask.api.mappers.factories.SortOrderFactory;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.GoodOrder;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.OrderRepository;
import by.temniakov.testtask.store.repositories.OrderRepositoryCustomImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = OrderService.class)
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    OrderRepositoryCustomImpl orderRepositoryCustom;
    @MockBean
    SortOrderFactory sortOrderFactory;
    @MockBean
    OrderMapper orderMapper;
    @MockBean
    GoodService goodService;
    @Mock
    List<GoodOrder> list;

    @Test
    void changeStatus_ShouldThrowEmptyOrderExceptionForEmptyDraftOrder(){
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(getEmptyDraftOrder()));

        assertThrows(
                EmptyOrderException.class,
                ()->orderService.changeOrderStatus(1,"ACTIVE"));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class,names = "ACTIVE",mode = EnumSource.Mode.EXCLUDE)
    void changeStatus_ShouldThrowUpdateOrderStatusExceptionForDraftOrder(Status status){
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(getEmptyDraftOrder()));

        assertThrows(
                UpdateOrderStatusException.class,
                ()->orderService.changeOrderStatus(1,status.toString()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class,names = {"CANCELLED","COMPLETED"},mode = EnumSource.Mode.EXCLUDE)
    void changeStatus_ShouldThrowUpdateOrderStatusExceptionForDraftActive(Status status){
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(getEmptyOrderByStatus(Status.ACTIVE)));

        assertThrows(
                UpdateOrderStatusException.class,
                ()->orderService.changeOrderStatus(1,status.toString()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class)
    void changeStatus_ShouldThrowUpdateOrderStatusExceptionForCancelledAndCompleted(Status status){
        assertAll(()->{
                    when(orderRepository.findById(anyInt()))
                            .thenReturn(Optional.of(getEmptyOrderByStatus(Status.CANCELLED)));
                    assertThrows(UpdateOrderStatusException.class,
                            ()->orderService.changeOrderStatus(1,status.toString()));},
                ()->{
                    when(orderRepository.findById(anyInt()))
                            .thenReturn(Optional.of(getEmptyOrderByStatus(Status.COMPLETED)));
                    assertThrows(UpdateOrderStatusException.class,
                            ()->orderService.changeOrderStatus(1,status.toString()));
                });
    }

    @Test
    void changeStatus_ShouldChangeStatusToActiveForDraftOrder(){
        Orders order = getNotEmptyOrder();
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(order));
        when(order.getGoodAssoc().isEmpty()).thenReturn(false);
        try {
            orderService.changeOrderStatus(1,Status.ACTIVE.toString());
        }catch (Exception ignored){}

        verify(orderRepository).findById(1);
        verify(order.getGoodAssoc()).isEmpty();
        assertSame(Status.ACTIVE,order.getStatus());
    }

    @Test
    void changeStatus_ShouldChangeStatusToCompleteForActiveOrder(){
        Orders order = getEmptyOrderByStatus(Status.ACTIVE);
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(order));
        try {
            orderService.changeOrderStatus(1,Status.COMPLETED.toString());
        }catch (Exception ignored){}

        verify(orderRepository).findById(1);
        assertSame(Status.COMPLETED,order.getStatus());
    }

    @Test
    void changeStatus_ShouldChangeStatusToCancelledForActiveOrder(){
        Orders order = getEmptyOrderByStatus(Status.ACTIVE);
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(order));
        try {
            orderService.changeOrderStatus(1,Status.CANCELLED.toString());
        }catch (Exception ignored){}

        verify(orderRepository).findById(1);
        assertSame(Status.CANCELLED,order.getStatus());
    }

    private Orders getEmptyDraftOrder(){
        return Orders.builder().build();
    }

    private Orders getEmptyOrderByStatus(Status status){
        return Orders.builder()
                .status(status)
                .build();
    }

    private Orders getNotEmptyOrder(){
        return Orders.builder()
                .goodAssoc(list)
                .build();
    }
}