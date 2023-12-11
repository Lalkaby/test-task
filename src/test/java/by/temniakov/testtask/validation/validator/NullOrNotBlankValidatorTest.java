package by.temniakov.testtask.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NullOrNotBlankValidatorTest {

    @InjectMocks
    NullOrNotBlankValidator  nullOrNotBlankValidator;
    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @ParameterizedTest
    @ValueSource(strings={"","  ","\t","\n"})
    void isValid_ShouldReturnFalseForBlankStrings(String input) {
        Assertions.assertFalse(nullOrNotBlankValidator.isValid(input, constraintValidatorContext));
    }

    @ParameterizedTest
    @ValueSource(strings={"    Any string with spaces   ","c","Another one string"})
    void isValid_ShouldReturnTrueForNotBlankStrings(String input) {
        Assertions.assertTrue(nullOrNotBlankValidator.isValid(input, constraintValidatorContext));
    }

    @Test
    void isValid_ShouldReturnTrueForNullString(){
        Assertions.assertTrue(nullOrNotBlankValidator.isValid(null,constraintValidatorContext));
    }
}