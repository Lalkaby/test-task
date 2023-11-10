package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.dto.AddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AddressController {
    public static final String GET_ADDRESS = "/address/{id_address}";
    public static final String GET_ADDRESSES = "/addresses";
    public static final String CREATE_ADDRESS = "/addresses";
    public static final String DELETE_ADDRESS = "/addresses/{id_address}";


    @GetMapping(GET_ADDRESS)
    public ResponseEntity<AddressDTO> getAddress(
            @PathVariable(name = "id_address") String addressId){
        AddressDTO response = AddressDTO.builder()
                .street("some")
                .house("some")
                .city("some")
                .id(1).build()
                ;
        return ResponseEntity.of(Optional.empty());
    }

    @GetMapping(GET_ADDRESSES)
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        throw  new UnsupportedOperationException("Not implemented");
    }

    @PostMapping(CREATE_ADDRESS)
    public ResponseEntity<AddressDTO> createAddress(){
        throw  new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<AddressDTO> deleteAddress(
            @PathVariable(name = "id_address") String addressId){
        throw  new UnsupportedOperationException("Not implemented");
    }
}
