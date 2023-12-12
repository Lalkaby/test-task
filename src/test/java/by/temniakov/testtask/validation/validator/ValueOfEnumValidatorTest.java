package by.temniakov.testtask.validation.validator;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.ValueOfEnum;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraints.Mod10Check;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValueOfEnumValidatorTest {
    @InjectMocks
    static ValueOfEnumValidator valueOfEnumValidator;
    @Mock
    static ConstraintValidatorContext constraintValidatorContext;
    @Mock
    static ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @BeforeEach
    void setUpEach() {
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(constraintViolationBuilder);
    }

    @Test
    void isValid_ShouldReturnTrueForNull(){
        assertTrue(valueOfEnumValidator.isValid(null,constraintValidatorContext));
        verifyNoInteractions(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()));
    }

    @ParameterizedTest
    @EnumSource(City.class)
    void isValid_ShouldReturnTrueForCities(City city) {
        valueOfEnumValidator.initialize(enumCity());
        assertTrue(valueOfEnumValidator.isValid(city.toString(),constraintValidatorContext));
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void isValid_ShouldReturnTrueForCurrencies(Currency currency) {
        valueOfEnumValidator.initialize(enumCurrency());
        assertTrue(valueOfEnumValidator.isValid(currency.toString(),constraintValidatorContext));
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    void isValid_ShouldReturnTrueForStatuses(Status status) {
        valueOfEnumValidator.initialize(enumStatus());
        assertTrue(valueOfEnumValidator.isValid(status.toString(),constraintValidatorContext));
    }

    @ParameterizedTest
    @ValueSource(strings = {"MINSK","USD","BACKFLIP"})
    void isValid_ShouldReturnFalseForNotStatusStrings(String input){
        valueOfEnumValidator.initialize(enumStatus());
        assertFalse(valueOfEnumValidator.isValid(input,constraintValidatorContext));
    }


    @ValueOfEnum(enumClass = City.class)
    private ValueOfEnum enumCity() {
        try {
            return this.getClass()
                    .getDeclaredMethod("enumCity").getAnnotation(ValueOfEnum.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @ValueOfEnum(enumClass = Status.class)
    private ValueOfEnum enumStatus() {
        try {
            return this.getClass()
                    .getDeclaredMethod("enumStatus").getAnnotation(ValueOfEnum.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @ValueOfEnum(enumClass = Currency.class)
    private ValueOfEnum enumCurrency(){
        try {
            return this.getClass()
                    .getDeclaredMethod("enumCurrency").getAnnotation(ValueOfEnum.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}