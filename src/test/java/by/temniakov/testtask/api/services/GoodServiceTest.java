package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.mappers.GoodMapper;
import by.temniakov.testtask.api.mappers.factories.SortGoodFactory;
import by.temniakov.testtask.store.repositories.GoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.ExampleMatcher;

import static org.junit.jupiter.api.Assertions.*;

class GoodServiceTest {
    GoodService goodService;

    GoodRepository goodRepository;
    GoodMapper goodMapper;
    SortGoodFactory sortGoodFactory;

    @BeforeEach
    void setUp() {
        goodMapper = Mockito.mock(GoodMapper.class);

        goodService = new GoodService(goodRepository,goodMapper,sortGoodFactory);
    }

    @Test
    void getDtoByIdOrThrowException() {
        goodService.getDtoByIdOrThrowException(1);
    }
}