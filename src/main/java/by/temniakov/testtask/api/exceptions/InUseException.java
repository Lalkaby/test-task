package by.temniakov.testtask.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class InUseException extends RuntimeException{
    private final Integer id;

    public InUseException(String message, Integer id){
        super(message);
        this.id = id;
    }
}
