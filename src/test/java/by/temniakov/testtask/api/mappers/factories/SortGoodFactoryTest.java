package by.temniakov.testtask.api.mappers.factories;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

class SortGoodFactoryTest {
    static SortGoodFactory sortGoodFactory;

    @BeforeAll
    static void setUp(){
        sortGoodFactory = new SortGoodFactory();
    }

    @ParameterizedTest
    @MethodSource("provideOrderGoodsForMapping")
    void fromJsonSortOrder_ShouldReturnSnakeProperties(Sort.Order orderGoodInput, String propertyExpected){
        Assertions.assertEquals(
                propertyExpected,
                sortGoodFactory.fromJsonSortOrder(orderGoodInput).getProperty());
    }

    @Test
    void fromJsonSortOrder_ShouldThrowsIllegalArgumentException(){
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"not_existing_key");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->sortGoodFactory.fromJsonSortOrder(order));
    }

    private static Stream<Arguments> provideOrderGoodsForMapping(){
        return Stream.of(
                Arguments.of(
                        new Sort.Order(Sort.Direction.DESC,"price"),
                       "price"),
                Arguments.of(
                        new Sort.Order(Sort.Direction.ASC,"amount"),
                        "amount")
        );
    }
}
