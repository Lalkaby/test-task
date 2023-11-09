package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.stream.Stream;

public interface OrderRepository
        extends JpaRepository<OrderEntity,Integer>, JpaSpecificationExecutor<OrderEntity> {

    Stream<OrderEntity> findAllByPhoneNumberContaining(String filterString, Pageable pageable);
}
