package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Orders;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface OrderRepository
        extends JpaRepository<Orders,Integer>, JpaSpecificationExecutor<Orders> {
    @Nonnull
    @EntityGraph(attributePaths = {"address"})
    Page<Orders> findAll(@Nonnull Pageable pageable);
    Stream<Orders> findAllByPhoneNumberContaining(String filterString, Pageable pageable);

    @Query(nativeQuery = true,value = "select status from orders where orders.id=:orderId")
    Status getStatusByOrderId(Integer orderId);
}
