package by.temniakov.testtask.api.controllers.helpers;

import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.entities.Order;
import by.temniakov.testtask.store.repositories.AddressRepository;
import by.temniakov.testtask.store.repositories.GoodRepository;
import by.temniakov.testtask.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ControllerHelper {
    private final GoodRepository goodRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    public Good getGoodOrThrowException(Integer goodId){
        return goodRepository
                .findById(goodId)
                .orElseThrow(()->
                        new NotFoundException("Good doesn't exists.",goodId)
                );
    }

    public Order getOrderOrThrowException(Integer orderId){
        return orderRepository
                .findById(orderId)
                .orElseThrow(()->
                        new NotFoundException("Order doesn't exists.", orderId)
                );
    }


    public Address getAddressOrThrowException(Integer addressId){
        return addressRepository
                .findById(addressId)
                .orElseThrow(()->
                        new NotFoundException("Address doesn't exists.", addressId)
                );
    }
}
