package by.temniakov.testtask.validation.validator;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.CityEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CityEnumValidator implements ConstraintValidator<CityEnum, City> {
    private City[] subset;

    @Override
    public void initialize(CityEnum constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(City value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
