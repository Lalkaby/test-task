package by.temniakov.testtask.validation.annotation;

import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.validation.validator.CurrencyEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CurrencyEnumValidator.class)
public @interface CurrencyEnum {
    Currency[] anyOf();
    boolean nullable() default false;
    String message() default "must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
