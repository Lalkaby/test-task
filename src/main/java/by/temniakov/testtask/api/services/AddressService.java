package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.AddressDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.api.exceptions.OrderStatusException;
import by.temniakov.testtask.api.mappers.AddressMapper;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.store.repositories.AddressRepository;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    @Getter
    private final ExampleMatcher exampleMatcherWithIgnoreIdPath;


    public void delete(Integer addressId){
        checkExistsAndThrowException(addressId);

        checkInUseAndThrowException(addressId);

        addressRepository.deleteById(addressId);
    }

    public Page<Address> findAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public List<AddressDto> findDtoByPage(
            @Min(value = 0, message = "must be not less than 0") Integer page,
            @Min(value = 1, message = "must be not less than 1") Integer size) {
        return findAll(PageRequest.of(page,size))
                .map(addressMapper::toDto)
                .toList();
    }

    public AddressDto getDtoByIdOrThrowException(Integer addressId) {
        return addressMapper
                .toDto(getByIdOrThrowException(addressId));

    }

    public Address getByIdOrThrowException(Integer addressId){
        return addressRepository
                .findById(addressId)
                .orElseThrow(()->
                        new NotFoundException("Address doesn't exists.", addressId)
                );
    }

    public void checkExistsAndThrowException(Integer addressId) {
        if (!addressRepository.existsById(addressId)){
            throw new NotFoundException("Address doesn't exists.", addressId);
        }
    }

    public Address getUpdatedAddress(
            Integer addressId, @Validated(value = {UpdateInfo.class, IdNullInfo.class, Default.class}) AddressDto addressDto) {
        Address address = getByIdOrThrowException(addressId);

        Address cloneAddress = addressMapper.clone(address);
        addressMapper.updateFromDto(addressDto,cloneAddress);
        Address savedAddress = cloneAddress;

        if (!cloneAddress.equals(address)){
            savedAddress = getUpdatedOrExistingAddress(cloneAddress);
        }

        return savedAddress;
    }


    public Address createAddress(
            @Validated(value = {CreationInfo.class, Default.class}) AddressDto createAddressDto) {
        Address address = addressMapper.fromDto(createAddressDto);
        return addressRepository
                .findOne(Example.of(address))
                .orElseGet(()->addressRepository.saveAndFlush(address));
    }

    public AddressDto getDtoFromAddress(Address address) {
        return addressMapper.toDto(address);
    }

    private Address getUpdatedOrExistingAddress(Address cloneAddress) {
        return addressRepository
                .findOne(Example.of(cloneAddress, exampleMatcherWithIgnoreIdPath))
                .orElseGet(()->addressRepository.saveAndFlush(cloneAddress));
    }

    private void checkInUseAndThrowException(Integer addressId) {
        if (addressRepository.countOrdersWithAddressById(addressId)!=0){
            throw new InUseException("Address is still in use",addressId);
        }
    }
}
