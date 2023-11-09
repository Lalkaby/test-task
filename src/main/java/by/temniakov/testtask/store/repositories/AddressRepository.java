package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.AddressEntity;
import by.temniakov.testtask.store.enums.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface AddressRepository
        extends JpaRepository<AddressEntity,Integer> {
    Stream<AddressEntity> streamAllByCity(City city);
    Stream<AddressEntity> streamAllByStreet(String street);
}
