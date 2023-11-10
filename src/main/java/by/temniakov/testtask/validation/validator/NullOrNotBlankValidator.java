package by.temniakov.testtask.validation.validator;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    @Override
    public void initialize(NullOrNotBlank parameters) {
        //no-op
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (null == value) {
            return true;
        }
        if (value.isEmpty()) {
            return false;
        }

        boolean isAllWhitespace = value.matches("^\\s*$");
        return !isAllWhitespace;
    }
}