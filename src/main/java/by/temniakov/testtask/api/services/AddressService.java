package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.InAddressDto;
import by.temniakov.testtask.api.dto.OutAddressDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.api.mappers.AddressMapper;
import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.store.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;


    public void delete(Integer addressId){
        checkExistsAndThrowException(addressId);

        checkInUseAndThrowException(addressId);

        deleteById(addressId);
    }

    private void deleteById(Integer addressId) {
        addressRepository.deleteById(addressId);
    }

    public Page<Address> findAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public List<OutAddressDto> findDtoByPage(Integer page, Integer size) {
        return findAll(PageRequest.of(page,size))
                .map(addressMapper::toOutDto)
                .toList();
    }

    public OutAddressDto getDtoByIdOrThrowException(Integer addressId) {
        return addressMapper
                .toOutDto(getByIdOrThrowException(addressId));

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

    public Address getUpdatedOrExistingAddress(
            Integer addressId, InAddressDto addressDto) {
        Address address = getByIdOrThrowException(addressId);

        Address cloneAddress = addressMapper.clone(address);
        addressMapper.updateFromDto(addressDto,cloneAddress);
        Address savedAddress = cloneAddress;

        if (!cloneAddress.equals(address)){
            savedAddress = getUpdatedOrExistingAddress(cloneAddress);
        }

        return savedAddress;
    }

    public Address createAddress(InAddressDto createAddressDto) {
        Address address = addressMapper.fromDto(createAddressDto);
        return addressRepository
                .findOne(Example.of(address))
                .orElseGet(()->addressRepository.saveAndFlush(address));
    }

    public OutAddressDto getDtoFromAddress(Address address) {
        return addressMapper.toOutDto(address);
    }

    private Address getUpdatedOrExistingAddress(Address cloneAddress) {
        return addressRepository
                .findOne(Example.of(cloneAddress, ExampleMatcher.matching().withIgnorePaths("id")))
                .orElseGet(()->addressRepository.saveAndFlush(cloneAddress));
    }

    private void checkInUseAndThrowException(Integer addressId) {
        if (addressRepository.countOrdersWithAddressById(addressId)!=0){
            throw new InUseException("Address is still in use",addressId);
        }
    }
}
