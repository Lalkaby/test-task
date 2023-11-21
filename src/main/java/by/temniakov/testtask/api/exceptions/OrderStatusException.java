package by.temniakov.testtask.api.exceptions;

import by.temniakov.testtask.enums.Status;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderStatusException extends OrderException {
    private final Integer id;
    private final Status currentStatus;

    public OrderStatusException(String message, Integer id, Status currentStatus) {
        super(message);
        this.id = id;
        this.currentStatus = currentStatus;
    }
}
