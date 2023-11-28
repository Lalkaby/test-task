package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.store.entities.Good;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface GoodRepository
        extends JpaRepository<Good,Integer> {
    Stream<Good> findAllByPriceLessThan(Double price, Pageable pageable);
    Stream<Good> findAllByPriceGreaterThan(Double price, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select count(*) from good_order where id_good=:goodId")
    Integer countOrdersWithGoodById(Integer goodId);

    @Query(value = "SELECT g FROM Good g LEFT JOIN FETCH g.orderAssoc")
    Page<Good> findAllWithOrders(Pageable pageable);

    @Query(nativeQuery = true,
            value = "select good.id from good where (good.id in :goodIds)")
    List<Integer> getExistingIds(List<Integer> goodIds);
}
