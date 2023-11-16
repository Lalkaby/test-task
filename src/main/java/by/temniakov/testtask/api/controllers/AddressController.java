package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.AddressDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.mappers.AddressMapper;
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
@Transactional
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {
    private final AddressMapper addressMapper;
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

    @GetMapping(FETCH_ADDRESSES)
    public ResponseEntity<List<AddressDto>> fetchAddresses(
            @RequestParam(name = "page", defaultValue = "0")
            @Min(value = 0, message = "must be not less than 0") Integer page,
            @RequestParam(name = "size", defaultValue = "50")
            @Min(value = 1,message = "must be not less than 1") Integer size){
        return ResponseEntity.of(
                Optional.of(
                        addressRepository
                                .findAll(PageRequest.of(page,size))
                                .map(addressMapper::toDto)
                                .toList()
                )
        );
    }

    @PatchMapping(UPDATE_ADDRESS)
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable(name = "id_address") Integer addressId,
            @RequestBody @Validated(value = {UpdateInfo.class, IdNullInfo.class, Default.class}) AddressDto addressDto)
    {
        Address address = controllerHelper.getAddressOrThrowException(addressId);

        Address cloneAddress = addressMapper.clone(address);
        addressMapper.updateFromDto(addressDto,cloneAddress);
        Address savedAddress = cloneAddress;
        if (!cloneAddress.equals(address)){
            savedAddress = addressRepository
                    .findOne(Example.of(cloneAddress, controllerHelper.getExampleMatcherWithIgnoreIdPath()))
                    .orElseGet(()->addressRepository.saveAndFlush(cloneAddress));
        }

        return ResponseEntity.of(Optional.of(savedAddress).map(addressMapper::toDto));
    }


    @PostMapping(CREATE_ADDRESS)
    public ResponseEntity<AddressDto> createAddress(
            @RequestBody @Validated(value = {CreationInfo.class, Default.class}) AddressDto createAddressDto){
        Address address = addressMapper.fromDto(createAddressDto);
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
