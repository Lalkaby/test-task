package by.temniakov.testtask.api.mappers.factories;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

class SortOrderFactoryTest {
    static SortOrderFactory sortOrderFactory;

    @BeforeAll
    static void setUp(){
        sortOrderFactory = new SortOrderFactory();
    }

    @ParameterizedTest
    @MethodSource("provideOrderOrdersForMapping")
    void fromJsonSortOrder_ShouldReturnSnakeProperties(Sort.Order orderOrdersInput, String propertyExpected){
        Assertions.assertEquals(
                propertyExpected,
                sortOrderFactory.fromJsonSortOrder(orderOrdersInput).getProperty());
    }

    @Test
    void fromJsonSortOrder_ShouldThrowIllegalArgumentException(){
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"not_existing_key");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->sortOrderFactory.fromJsonSortOrder(order));
    }

    private static Stream<Arguments> provideOrderOrdersForMapping(){
        return Stream.of(
                Arguments.of(
                        new Sort.Order(Sort.Direction.DESC,"phone_number"),
                        "phoneNumber"),
                Arguments.of(
                        new Sort.Order(Sort.Direction.ASC,"order_time"),
                        "orderTime"),
                Arguments.of(
                        new Sort.Order(Sort.Direction.ASC,"username"),
                        "username")
        );
    }
}