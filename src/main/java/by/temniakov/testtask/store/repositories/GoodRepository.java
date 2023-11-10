package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.GoodEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface GoodRepository
        extends JpaRepository<GoodEntity,Integer> {
    Stream<GoodEntity> findAllByPriceLessThan(Double price, Pageable pageable);
    Stream<GoodEntity> findAllByPriceGreaterThan(Double price, Pageable pageable);

    

}
