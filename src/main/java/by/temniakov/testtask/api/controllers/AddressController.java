package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.AddressDto;
import by.temniakov.testtask.api.dto.CreateAddressDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.mappers.AddressMapper;
import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.repositories.AddressRepository;
import by.temniakov.testtask.store.repositories.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AddressController {
    private final AddressMapper addressMapper;
    private final OrderRepository orderRepository;
    private final ControllerHelper controllerHelper;
    private final AddressRepository addressRepository;

    public static final String GET_ADDRESS = "/addresses/{id_address}";
    public static final String FETCH_ADDRESSES = "/addresses";
    public static final String UPDATE_ADDRESS = "/addresses/{id_address}";
    public static final String CREATE_ADDRESS = "/addresses";
    public static final String DELETE_ADDRESS = "/addresses/{id_address}";


    @GetMapping(GET_ADDRESS)
    public ResponseEntity<AddressDto> getAddress(
            @PathVariable(name = "id_address") Integer addressId){
        Address address = controllerHelper.getAddressOrThrowException(addressId);
        return ResponseEntity.of(Optional.of(address).map(addressMapper::toDto));
    }

    @PatchMapping(UPDATE_ADDRESS)
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable(name = "id_address") Integer addressId, @RequestBody @Valid AddressDto addressDto)
    {
        Address address = controllerHelper.getAddressOrThrowException(addressId);

        Address cloneAddress = addressMapper.clone(address);
        addressMapper.updateFromDto(addressDto,address);
        if (!cloneAddress.equals(address)){
            addressMapper.updateFromDto(addressDto,cloneAddress);
            address = addressRepository
                    .findOne(Example.of(address, ExampleMatcher.matching().withIgnorePaths("orders")))
                    .orElseGet(()->addressRepository.saveAndFlush(cloneAddress));
        }

        return ResponseEntity.of(Optional.of(address).map(addressMapper::toDto));
    }

    @PostMapping(CREATE_ADDRESS)
    public ResponseEntity<AddressDto> createAddress(
            @RequestBody @Valid CreateAddressDto createAddressDTO){
        Address address = addressMapper.fromDto(createAddressDTO);
        Address savedAddress = addressRepository
                .findOne(Example.of(address))
                .orElseGet(()->addressRepository.saveAndFlush(address));

        return ResponseEntity.of(Optional.of(savedAddress).map(addressMapper::toDto));
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<AddressDto> deleteAddress(
            @PathVariable(name = "id_address") Integer addressId){
        Address address = controllerHelper.getAddressOrThrowException(addressId);

        if (!address.getOrders().isEmpty()){
            throw new InUseException("Address is still in use",addressId);
        }

        addressRepository.delete(address);

        return ResponseEntity.ok().build();
    }
}
