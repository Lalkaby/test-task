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

@ExtendWith(MockitoExtension.class)
class ValueOfEnumValidatorTest {
    @InjectMocks
    static ValueOfEnumValidator valueOfEnumValidator;
    @Mock
    static ConstraintValidatorContext constraintValidatorContext;
    static ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @BeforeAll
    static void setUp() {
        valueOfEnumValidator = new ValueOfEnumValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintViolationBuilder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        Mockito.when(constraintValidatorContext.buildConstraintViolationWithTemplate(Mockito.anyString()))
                .thenReturn(constraintViolationBuilder);
    }

    @ParameterizedTest
    @EnumSource(City.class)
    void isValid_ShouldReturnTrueForCities(City city) {
        valueOfEnumValidator.initialize(enumCity());
        Assertions.assertTrue(valueOfEnumValidator.isValid(city.toString(),constraintValidatorContext));
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void isValid_ShouldReturnTrueForCurrencies(Currency currency) {
        valueOfEnumValidator.initialize(enumCurrency());
        Assertions.assertTrue(valueOfEnumValidator.isValid(currency.toString(),constraintValidatorContext));
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    void isValid_ShouldReturnTrueForStatuses(Status status) {
        valueOfEnumValidator.initialize(enumStatus());
        Assertions.assertTrue(valueOfEnumValidator.isValid(status.toString(),constraintValidatorContext));
    }

    @Test
    void isValid_ShouldReturnTrueForNull(){
        Assertions.assertTrue(valueOfEnumValidator.isValid(null,constraintValidatorContext));
    }

    @ParameterizedTest
    @ValueSource(strings = {"MINSK","USD","BACKFLIP"})
    void isValid_ShouldReturnFalseForNotStatusStrings(String input){
        valueOfEnumValidator.initialize(enumStatus());
        Assertions.assertFalse(valueOfEnumValidator.isValid(input,constraintValidatorContext));
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