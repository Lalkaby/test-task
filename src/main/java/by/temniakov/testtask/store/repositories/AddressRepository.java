package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.AddressEntity;
import by.temniakov.testtask.enums.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface AddressRepository
        extends JpaRepository<AddressEntity,Integer> {


    //List<City> getCitiesStartsWith(String prefixText);
    Stream<AddressEntity> streamAllByStreet(String street);

//    List<City> getCities();
}
