package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.AddressDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.mappers.AddressMapper;
import by.temniakov.testtask.api.services.AddressService;
import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.store.repositories.AddressRepository;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    public ResponseEntity<AddressDto> getAddress(
            @PathVariable(name = "id_address") Integer addressId){
        return ResponseEntity.of(Optional.of(addressService.getDtoByIdOrThrowException(addressId)));
    }

    @GetMapping(FETCH_ADDRESSES)
    public ResponseEntity<List<AddressDto>> fetchAddresses(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size){
        return ResponseEntity.of(
                Optional.of(addressService.findDtoByPage(page,size)));
    }

    @PatchMapping(UPDATE_ADDRESS)
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable(name = "id_address") Integer addressId,
            @RequestBody  AddressDto addressDto) {

        AddressDto updatedAddressDto = addressService
                .getDtoFromAddress(addressService.getUpdatedAddress(addressId, addressDto));

        return ResponseEntity.of(
                Optional.of(updatedAddressDto));
    }

    @PostMapping(CREATE_ADDRESS)
    public ResponseEntity<AddressDto> createAddress(
            @RequestBody AddressDto createAddressDto){
        AddressDto createdAddressDto = addressService
                .getDtoFromAddress(addressService.createAddress(createAddressDto));
        return ResponseEntity.of(Optional.of(createdAddressDto));
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<AddressDto> deleteAddress(
            @PathVariable(name = "id_address") Integer addressId){
        addressService.delete(addressId);

        return ResponseEntity.ok().build();
    }
}
