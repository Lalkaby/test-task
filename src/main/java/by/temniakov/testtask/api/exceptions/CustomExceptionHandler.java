package by.temniakov.testtask.api.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail notFoundHandler(NotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404"));
        problemDetail.setTitle("Not found.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("id", exception.getId());
        return problemDetail;
    }

    @ExceptionHandler(InUseException.class)
    public ProblemDetail entityInUseHandler(InUseException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setType(URI.create("https://developer.mozilla.org/ru/docs/Web/HTTP/Status/409"));
        problemDetail.setTitle("In use");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("id",exception.getId());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validationHandler(MethodArgumentNotValidException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Validation error.");
        List<FieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ex -> new FieldError(ex.getField(),ex.getDefaultMessage()))
                .toList();
        problemDetail.setProperty("invalidFields",fieldErrors);
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail validationHandler(ConstraintViolationException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Validation error.");
        List<FieldError> fieldErrors = exception.getConstraintViolations()
                .stream()
                .map(ex -> {
                    String input = ex.getPropertyPath().toString();
                    int dotIndex = input.indexOf(".");
                    if (dotIndex != -1) {
                        input = input.substring(dotIndex + 1);
                    }
                    return new FieldError(input, ex.getMessage());
                })
                .toList();
        problemDetail.setProperty("invalidFields",fieldErrors);
        return problemDetail;
    }

//    @Order()
//    @ExceptionHandler(Exception.class)
//    public ProblemDetail anyExceptionHandler(Exception ex){
//        ProblemDetail problemDetail = ProblemDetail.forStatus(200);
//        problemDetail.setDetail(ex.getClass().toString() + ex.getMessage() + Arrays.toString(ex.getStackTrace()));
//        System.err.println(Arrays.toString(ex.getStackTrace()));
//        return problemDetail;
//    }

    record FieldError(String field, String message){}
}
