package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void refresh(Orders order) {
        entityManager.refresh(order);
    }

    @Override
    public void detachOrder(Orders order) {
        entityManager.detach(order);
    }
}
