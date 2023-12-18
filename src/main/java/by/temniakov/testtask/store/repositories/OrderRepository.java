package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Orders;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface OrderRepository
        extends JpaRepository<Orders,Integer>, JpaSpecificationExecutor<Orders> {

    @Query(value = "SELECT o FROM Orders o LEFT JOIN FETCH o.address")
    List<Orders> findAllBy(Pageable pageable);

    @Query(value = "SELECT o FROM Orders o LEFT JOIN FETCH o.address where o.phoneNumber like %:substring%")
    List<Orders> findAllByPhoneNumberContaining(String substring, Pageable pageable);

//    @EntityGraph(attributePaths = {"address"})
//    Page<Orders> findAll(Example<Orders> example, Pageable pageable);

    @Query(nativeQuery = true,value = "select status from orders where orders.id=:orderId")
    Status getOrderStatusById(Integer orderId);

    @Query(nativeQuery = true,
            value = "select t.id,coalesce(sum(amount),0) " +
                    "from (select id from orders where id in (:orderIds)) t " +
                    "left join good_order on t.id=good_order.id_order " +
                    "group by t.id")
    List<Tuple> getOrdersAmountOfGoods(List<Integer> orderIds);
}
