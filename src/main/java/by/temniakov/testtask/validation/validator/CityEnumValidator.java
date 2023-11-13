package by.temniakov.testtask.validation.validator;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.validation.annotation.CityEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CityEnumValidator implements ConstraintValidator<CityEnum, City> {
    private City[] subset;
    private boolean nullable;

    @Override
    public void initialize(CityEnum constraint) {
        this.subset = constraint.anyOf();
        this.nullable = constraint.nullable();
    }

    @Override
    public boolean isValid(City value, ConstraintValidatorContext context) {
        if (nullable && value == null){
            return true;
        }
        return value != null && Arrays.asList(subset).contains(value);
    }
}
