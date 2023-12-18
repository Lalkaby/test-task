package by.temniakov.testtask.store.repositories;

import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface GoodRepository
        extends JpaRepository<Good,Integer> {
    @Query(nativeQuery = true,
            value = "select count(*) from good_order where id_good=:goodId")
    Integer countOrdersWithGoodById(Integer goodId);

    @Query(value = "SELECT g FROM Good g LEFT JOIN FETCH g.orderAssoc")
    List<Good> findAllWithOrders(Pageable pageable);

    List<Good> findAllBy(Pageable pageable);

    @Query(nativeQuery = true,
            value = "select good.id from good where (good.id in :goodIds)")
    List<Integer> getExistingIds(List<Integer> goodIds);

    @Query(nativeQuery = true,
            value = "select t.id,count(id_order) " +
                    "from (select id from good where good.id in (:goodIds)) t " +
                    "left join good_order on t.id=good_order.id_good " +
                    "where id_order in ( select id from orders where status=:#{#status.toString()}) " +
                    "or id_order is null " +
                    "group by t.id")
    List<Tuple> getCountGoodByOrderStatus(List<Integer> goodIds, Status status);
}
