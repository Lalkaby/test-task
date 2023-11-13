package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.enums.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface AddressRepository
        extends JpaRepository<Address,Integer> {


    //List<City> getCitiesStartsWith(String prefixText);
    Stream<Address> streamAllByStreet(String street);

//    List<City> getCities();
}
