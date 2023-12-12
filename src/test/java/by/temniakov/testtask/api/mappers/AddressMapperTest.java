package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.api.dto.InAddressDto;
import by.temniakov.testtask.api.dto.OutAddressDto;
import by.temniakov.testtask.store.entities.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AddressMapperTest {
    @Spy
    AddressMapper addressMapper;


    @Test
    void toOutDto() {
    }

    @Test
    void fromDto() {
    }

    @Test
    void updateFromDto() {
    }

    @Test
    void testClone() {
    }

    InAddressDto getInAddressDto(){
        return InAddressDto.builder()
                .city("MINSK")
//                .house()
//                .street()
                .build();
    }
    Address getAddress(){
        return null;
    }
    OutAddressDto getOutAddressDto(){
        return null;
    }

}