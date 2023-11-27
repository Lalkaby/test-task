package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.dto.InAddressDto;
import by.temniakov.testtask.api.dto.OutAddressDto;
import by.temniakov.testtask.api.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    public static final String GET_ADDRESS = "/addresses/{id_address}";
    public static final String FETCH_ADDRESSES = "/addresses";
    public static final String UPDATE_ADDRESS = "/addresses/{id_address}";
    public static final String CREATE_ADDRESS = "/addresses";
    public static final String DELETE_ADDRESS = "/addresses/{id_address}";


    @GetMapping(GET_ADDRESS)
    public ResponseEntity<OutAddressDto> getAddress(
            @PathVariable(name = "id_address") Integer addressId){
        return ResponseEntity.of(Optional.of(addressService.getDtoByIdOrThrowException(addressId)));
    }

    @GetMapping(FETCH_ADDRESSES)
    public ResponseEntity<List<OutAddressDto>> fetchAddresses(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size){
        return ResponseEntity.of(
                Optional.of(addressService.findDtoByPage(page,size)));
    }

    @PatchMapping(UPDATE_ADDRESS)
    public ResponseEntity<OutAddressDto> updateAddress(
            @PathVariable(name = "id_address") Integer addressId,
            @RequestBody InAddressDto addressDto) {

        OutAddressDto updatedAddressDto = addressService
                .getDtoFromAddress(addressService.getUpdatedOrExistingAddress(addressId, addressDto));

        return ResponseEntity.of(
                Optional.of(updatedAddressDto));
    }

    @PostMapping(CREATE_ADDRESS)
    public ResponseEntity<OutAddressDto> createAddress(
            @RequestBody InAddressDto createAddressDto){
        OutAddressDto createdAddressDto = addressService
                .getDtoFromAddress(addressService.createAddress(createAddressDto));
        return ResponseEntity.of(Optional.of(createdAddressDto));
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<OutAddressDto> deleteAddress(
            @PathVariable(name = "id_address") Integer addressId){
        addressService.delete(addressId);

        return ResponseEntity.ok().build();
    }
}
