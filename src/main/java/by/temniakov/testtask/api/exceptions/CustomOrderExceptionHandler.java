package by.temniakov.testtask.api.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class CustomOrderExceptionHandler {
    @Order(1)
    @ExceptionHandler(UpdateOrderStatusException.class)
    public ProblemDetail orderStatusHandler(UpdateOrderStatusException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Invalid status.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("currentStatus",exception.getCurrentStatus());
        problemDetail.setProperty("newStatus",exception.getNewStatus());
        problemDetail.setProperty("orderId",exception.getId());
        return problemDetail;
    }

    @Order(1)
    @ExceptionHandler(EmptyOrderException.class)
    public ProblemDetail emptyOrderHandler(EmptyOrderException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Empty order.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("orderId",exception.getId());
        return problemDetail;
    }

    @Order(1)
    @ExceptionHandler(InvalidOrderAmountException.class)
    public ProblemDetail invalidOrderAmountHandler(InvalidOrderAmountException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Invalid order amount.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("orderId",exception.getOrderId());
        problemDetail.setProperty("invalidGoods", exception.getInvalidGoodOrders());

        return problemDetail;
    }

    @Order
    @ExceptionHandler(OrderStatusException.class)
    public ProblemDetail orderStatusHandler(OrderStatusException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://developer.mozilla.org/ru/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Status problem.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("currentStatus",exception.getCurrentStatus());
        problemDetail.setProperty("orderId",exception.getId());
        return problemDetail;
    }
}
