package by.temniakov.testtask.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EmptyOrderException extends OrderException {
    private final Integer id;

    public EmptyOrderException(String message, Integer id) {
        super(message);
        this.id = id;
    }
}
