package by.temniakov.testtask.validation.validator;

import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.validation.annotation.CurrencyEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CurrencyEnumValidator implements ConstraintValidator<CurrencyEnum, Currency> {
    private Currency[] subset;

    @Override
    public void initialize(CurrencyEnum constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Currency value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
