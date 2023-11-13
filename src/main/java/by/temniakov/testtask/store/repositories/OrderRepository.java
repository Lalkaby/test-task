package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.stream.Stream;

public interface OrderRepository
        extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order> {

    Stream<Order> findAllByPhoneNumberContaining(String filterString, Pageable pageable);
}
