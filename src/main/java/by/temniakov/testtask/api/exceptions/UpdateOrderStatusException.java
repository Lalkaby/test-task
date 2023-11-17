package by.temniakov.testtask.api.exceptions;

import by.temniakov.testtask.enums.Status;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UpdateOrderStatusException extends OrderStatusException {
    private final Status newStatus;

    public UpdateOrderStatusException(
            String message, Integer orderId, Status currentStatus,Status newStatus) {
        super(message, orderId, currentStatus);
        this.newStatus = newStatus;
    }
}
