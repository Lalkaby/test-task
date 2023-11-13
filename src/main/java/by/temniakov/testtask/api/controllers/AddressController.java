package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.AddressDto;
import by.temniakov.testtask.api.dto.CreateAddressDto;
import by.temniakov.testtask.api.mappers.AddressMapper;
import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.store.repositories.AddressRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
public class AddressController {
    private final AddressMapper addressMapper;
    private final ControllerHelper controllerHelper;
    private final AddressRepository addressRepository;

    public static final String GET_ADDRESS = "/addresses/{id_address}";
    private static final String FETCH_CITIES = "/cities";
    public static final String  FETCH_ADDRESSES = "/addresses";
    public static final String CREATE_ADDRESS = "/addresses";
    public static final String DELETE_ADDRESS = "/addresses/{id_address}";


    @GetMapping(GET_ADDRESS)
    public ResponseEntity<AddressDto> getAddress(
            @PathVariable(name = "id_address") Integer addressId){
        Address address = controllerHelper.getAddressOrThrowException(addressId);
        return ResponseEntity.of(Optional.of(address).map(addressMapper::toDto));
    }

//    @GetMapping(FETCH_CITIES)
//    public Response<List<City>> getCities(@RequestParam("prefix") Optional<String> optionalPrefix){
//        optionalPrefix = optionalPrefix.filter(prefix -> !prefix.trim().isEmpty());
//
//        List<City> cities = optionalPrefix
//                .map(addressRepository::getCitiesStartsWith)
//                .orElseGet(addressRepository::getCities);
//
//        return Response.of(Optional.of(cities));
//    }

    @PostMapping(CREATE_ADDRESS)
    public ResponseEntity<AddressDto> createAddress(
            @RequestBody @Valid CreateAddressDto createAddressDTO){
        Address address = addressMapper.fromDto(createAddressDTO);
        Address savedAddress = addressRepository
                .findOne(Example.of(address))
                .orElse(addressRepository.saveAndFlush(address));

        return ResponseEntity.of(Optional.of(savedAddress).map(addressMapper::toDto));
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<AddressDto> deleteAddress(
            @PathVariable(name = "id_address") String addressId){
        throw  new UnsupportedOperationException("Not implemented");
    }
}
