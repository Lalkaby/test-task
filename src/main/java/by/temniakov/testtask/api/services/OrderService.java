package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.exceptions.OrderStatusException;
import by.temniakov.testtask.api.exceptions.UpdateOrderStatusException;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Orders;
import by.temniakov.testtask.store.repositories.GoodRepository;
import by.temniakov.testtask.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                withdrawGoods();
            }
            case CANCELLED -> {
                restoreGoods();
            }
        }
    }

    private void withdrawGoods() {
        // TODO: 17.11.2023 make logic 
    }

    private void restoreGoods() {
        // TODO: 17.11.2023 make logic 
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
