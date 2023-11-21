package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.GoodOrder;
import by.temniakov.testtask.store.entities.GoodOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodOrderRepository extends JpaRepository<GoodOrder, GoodOrderId> {
}
