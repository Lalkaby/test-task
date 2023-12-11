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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderService orderService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderRepositoryCustomImpl orderRepositoryCustom;
    @Mock
    SortOrderFactory sortOrderFactory;
    @Mock
    OrderMapper orderMapper;
    @Mock
    GoodService goodService;



    @Test
    void changeStatus_ShouldThrowEmptyOrderExceptionForEmptyDraftOrder(){
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(getEmptyDraftOrder()));

        Assertions.assertThrows(
                EmptyOrderException.class,
                ()->orderService.changeOrderStatus(1,"ACTIVE"));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class,names = "ACTIVE",mode = EnumSource.Mode.EXCLUDE)
    void changeStatus_ShouldThrowUpdateOrderStatusExceptionForDraftOrder(Status status){
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(getEmptyDraftOrder()));

        Assertions.assertThrows(
                UpdateOrderStatusException.class,
                ()->orderService.changeOrderStatus(1,status.toString()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class,names = {"CANCELLED","COMPLETED"},mode = EnumSource.Mode.EXCLUDE)
    void changeStatus_ShouldThrowUpdateOrderStatusExceptionForDraftActive(Status status){
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(getEmptyOrderByStatus(Status.ACTIVE)));

        Assertions.assertThrows(
                UpdateOrderStatusException.class,
                ()->orderService.changeOrderStatus(1,status.toString()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class)
    void changeStatus_ShouldThrowUpdateOrderStatusExceptionForDraftCancelled(Status status){
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(getEmptyOrderByStatus(Status.CANCELLED)));

        Assertions.assertThrows(
                UpdateOrderStatusException.class,
                ()->orderService.changeOrderStatus(1,status.toString()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class)
    void changeStatus_ShouldThrowUpdateOrderStatusExceptionForDraftCompleted(Status status){
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(getEmptyOrderByStatus(Status.COMPLETED)));

        Assertions.assertThrows(
                UpdateOrderStatusException.class,
                ()->orderService.changeOrderStatus(1,status.toString()));
    }

    // TODO: 11.12.2023 add the remaining tests
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
        Assertions.assertSame(Status.ACTIVE,order.getStatus());
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
                .goodAssoc(getMockList())
                .build();
    }

    private List<GoodOrder> getMockList(){
        return Mockito.mock(List.class);
    }
}