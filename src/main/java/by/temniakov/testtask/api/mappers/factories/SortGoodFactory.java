package by.temniakov.testtask.api.mappers.factories;

import org.mapstruct.ObjectFactory;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SortGoodFactory {
    private static final Map<String, String> fieldMapping = new HashMap<>();
    static {
        fieldMapping.put("price", "price");
        fieldMapping.put("amount", "amount");
    }

    @ObjectFactory
    public Sort.Order fromJsonSortOrder(Sort.Order order){
        return new Sort.Order(order.getDirection(),fieldMapping.get(order.getProperty()));
    }

    public Set<String> getFilterKeys(){
        return fieldMapping.keySet();
    }
}
