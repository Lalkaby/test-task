package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderEventRepository extends JpaRepository<OrderEvent, UUID> {
    @Query(value = "select case when count(oe) > 0 then true else false end from OrderEvent oe")
    boolean existsAny();
}
