package by.temniakov.testtask.api.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail notFoundHandler(NotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Not found.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("id", exception.getId());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validationHandler(MethodArgumentNotValidException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation error.");
        List<FieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ex -> new FieldError(ex.getField(),ex.getDefaultMessage()))
                .toList();
        problemDetail.setProperty("invalidFields",fieldErrors);
        return problemDetail;
    }

    record FieldError(String field, String message){}
}
