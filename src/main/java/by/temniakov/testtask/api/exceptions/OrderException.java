package by.temniakov.testtask.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
