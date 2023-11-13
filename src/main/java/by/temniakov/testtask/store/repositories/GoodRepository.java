package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.Good;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface GoodRepository
        extends JpaRepository<Good,Integer> {
    Stream<Good> findAllByPriceLessThan(Double price, Pageable pageable);
    Stream<Good> findAllByPriceGreaterThan(Double price, Pageable pageable);

    

}
