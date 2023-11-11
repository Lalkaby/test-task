package by.temniakov.testtask.validation.validator;

import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.StatusEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class StatusEnumValidator implements ConstraintValidator<StatusEnum, Status> {
    private Status[] subset;

    @Override
    public void initialize(StatusEnum constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Status value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
