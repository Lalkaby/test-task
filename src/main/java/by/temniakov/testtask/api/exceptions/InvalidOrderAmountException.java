package by.temniakov.testtask.api.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidOrderAmountException extends OrderException {
    private final List<InvalidGoodOrder> invalidGoodOrders;
    private final Integer orderId;

    public InvalidOrderAmountException(String message,Integer orderId, List<InvalidGoodOrder> invalidGoodOrders) {
        super(message);
        this.invalidGoodOrders = invalidGoodOrders;
        this.orderId = orderId;
    }
}
