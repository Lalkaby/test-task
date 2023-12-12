package by.temniakov.testtask.api.dto;

import by.temniakov.testtask.validation.groups.CreationInfo;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Validator;

//@SpringBootTest
//@AutoConfigureTestEntityManager
class InAddressDtoTest {

//    @Autowired
//    Validator validator;

    @Test
    void testNoValidCity(){
        InAddressDto inAddressDto = getNoValidCity();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        var violations = validator.validate(inAddressDto, CreationInfo.class);
        System.out.println();
    }

    InAddressDto getNoValidCity(){
        return InAddressDto.builder()
                .city("VViTTebsk")
                .house("House")
                .street("Streeet")
                .build();
    }
}