package by.temniakov.testtask.api.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    private final List<Integer> ids;

    public NotFoundException(String message, Integer id){
        super(message);
        this.ids = List.of(id);
    }

    public NotFoundException(String message, List<Integer> ids){
        super(message);
        this.ids = ids;
    }
}