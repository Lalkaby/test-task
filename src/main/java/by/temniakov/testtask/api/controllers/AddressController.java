package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.dto.InAddressDto;
import by.temniakov.testtask.api.dto.OutAddressDto;
import by.temniakov.testtask.api.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name="address", description = "Address management APIs")
public class AddressController {
    private final AddressService addressService;

    public static final String GET_ADDRESS = "/addresses/{id_address}";
    public static final String FETCH_ADDRESSES = "/addresses";
    public static final String UPDATE_ADDRESS = "/addresses/{id_address}";
    public static final String CREATE_ADDRESS = "/addresses";
    public static final String DELETE_ADDRESS = "/addresses/{id_address}";

    @GetMapping(GET_ADDRESS)
    @Operation(
            tags = {"get","address"},
            summary = "Retrieve address by id",
            description = "Get a Address object by specifying its id")
    public ResponseEntity<OutAddressDto> getAddress(
            @Parameter(description = "Id of retrieving address", example = "1")
            @PathVariable(name = "id_address") Integer addressId){
        return ResponseEntity.of(Optional.of(addressService.getDtoByIdOrThrowException(addressId)));
    }

    @GetMapping(FETCH_ADDRESSES)
    @Operation(
            tags = {"get","address"},
            summary = "Retrieve addresses by page and size",
            description = "Fetch of address objects by page number and size of this page greater than 0")
    public ResponseEntity<List<OutAddressDto>> fetchAddresses(
            @Parameter(description = "Number of searching page", example = "0")
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Size of searching page", example = "50")
            @RequestParam(name = "size", defaultValue = "50") Integer size){
        return ResponseEntity.of(
                Optional.of(addressService.findDtoByPage(page,size)));
    }

    @PatchMapping(UPDATE_ADDRESS)
    @Operation(
            tags = {"patch","address"},
            summary = "Update existing address by id",
            description = "Update address by its id and address object or return already existing address with different id")
    public ResponseEntity<OutAddressDto> updateAddress(
            @Parameter(description = "Id of updated address", example = "1")
            @PathVariable(name = "id_address") Integer addressId,
            @RequestBody InAddressDto addressDto) {

        OutAddressDto updatedAddressDto = addressService
                .getDtoFromAddress(addressService.getUpdatedOrExistingAddress(addressId, addressDto));

        return ResponseEntity.of(
                Optional.of(updatedAddressDto));
    }

    @PostMapping(CREATE_ADDRESS)
    @Operation(
            tags = {"post","address"},
            summary = "Create a new address",
            description = "Create a new address from address object or return already existing address")
    public ResponseEntity<OutAddressDto> createAddress(
            @RequestBody InAddressDto createAddressDto){
        OutAddressDto createdAddressDto = addressService
                .getDtoFromAddress(addressService.createAddress(createAddressDto));
        return ResponseEntity.of(Optional.of(createdAddressDto));
    }

    @DeleteMapping(DELETE_ADDRESS)
    @Operation(
            tags = {"delete","address"},
            summary = "Remove address by Id",
            description = "Delete address by its id")
    public ResponseEntity<OutAddressDto> deleteAddress(
            @Parameter(description = "Address id to be deleted", example = "1")
            @PathVariable(name = "id_address") Integer addressId){
        addressService.delete(addressId);

        return ResponseEntity.ok().build();
    }
}
