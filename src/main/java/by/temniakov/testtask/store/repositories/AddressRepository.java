package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.Address;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface AddressRepository
        extends JpaRepository<Address,Integer> {
    Stream<Address> streamAllBy(Pageable pageable);

    @Query(nativeQuery = true,
    value = "select count(*) from orders where id_address=:addressId")
    Integer countOrdersWithAddressById(Integer addressId);
}
