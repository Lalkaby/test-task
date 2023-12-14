package by.temniakov.testtask.api.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail notFoundHandler(NotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404"));
        problemDetail.setTitle("Not found.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("ids", exception.getIds());
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

    @ExceptionHandler(InvalidStockLevelException.class)
    public ProblemDetail invalidGoodStockLevelHandler(InvalidStockLevelException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Invalid good stock level.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("goodId",exception.getGoodId());
        problemDetail.setProperty("currentLevel", exception.getCurrentLevel());

        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail validationHandler(ConstraintViolationException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Validation error.");
        List<ParamError> paramErrors = exception.getConstraintViolations()
                .stream()
                .map(ex -> {
                    String input = ex.getPropertyPath().toString();
                    int dotIndex = input.lastIndexOf(".");
                    if (dotIndex != -1) {
                        input = input
                                .substring(dotIndex + 1)
                                .replace("get", "")
                                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                                .replaceAll("([a-z])([A-Z])", "$1_$2")
                                .toLowerCase();
                    }
                    return new ParamError(input, ex.getMessage());
                })
                .toList();
        problemDetail.setProperty("invalidParams",paramErrors);
        return problemDetail;
    }

    record FieldError(String field, String message){}
    record ParamError(String param, String message){}
}
