package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.Address;
import by.temniakov.testtask.store.entities.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.stream.Stream;

public interface OrderRepository
        extends JpaRepository<Orders,Integer>, JpaSpecificationExecutor<Orders> {

    Optional<Orders> findByAddress_Id(Integer addressId);
    Stream<Orders> findAllByPhoneNumberContaining(String filterString, Pageable pageable);
}
