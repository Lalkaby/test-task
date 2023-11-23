package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.GoodOrder;
import by.temniakov.testtask.store.entities.GoodOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GoodOrderRepository extends JpaRepository<GoodOrder, GoodOrderId> {
    Optional<GoodOrder> findGoodOrderByOrder_IdAndGood_Id(Integer orderId, Integer goodId);
}
