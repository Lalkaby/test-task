package by.temniakov.testtask.validation.validator;


import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;
    private boolean nullable;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
        nullable = annotation.nullable();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable && value == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        String.format("must be one of %s", acceptedValues))
                .addConstraintViolation();

        return value != null && acceptedValues.contains(value.toString());
    }
}