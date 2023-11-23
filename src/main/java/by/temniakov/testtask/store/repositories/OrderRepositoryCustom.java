package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.Orders;

public interface OrderRepositoryCustom {
    void detachOrder(Orders order);

    void refresh(Orders order);
}
