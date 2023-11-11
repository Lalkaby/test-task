package by.temniakov.testtask.api.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    private final Integer id;
    public NotFoundException(String message, Integer id){
        super(message);
        this.id = id;
    }
}