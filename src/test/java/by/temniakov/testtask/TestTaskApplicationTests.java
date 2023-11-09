package by.temniakov.testtask;

import by.temniakov.testtask.store.repositories.GoodRepository;
import by.temniakov.testtask.store.repositories.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
class TestTaskApplicationTests {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    GoodRepository goodRepository;

    @Test
    @Transactional(readOnly = true)
    void filterNumberTest() {
        orderRepository.findAllByPhoneNumberContaining("529",PageRequest.of(0,100))
                .forEach(number->log.info(number.getPhoneNumber()));
    }

    @Test
    @Transactional(readOnly = true)
    void pageTest() {
        goodRepository
                .findAllByPriceLessThan(
                20.0,PageRequest.of(0,3, Sort.by("price").descending()))
                .forEach(good->log.info(good.getPrice()));
    }

}
