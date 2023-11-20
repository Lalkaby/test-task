package by.temniakov.testtask.api.mappers.factories;

import org.mapstruct.ObjectFactory;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SortOrderFactory {
    private static final Map<String, String> fieldMapping = new HashMap<>();
    static {
        fieldMapping.put("phone_number", "phoneNumber");
        fieldMapping.put("order_time", "orderTime");
        fieldMapping.put("username", "username");
    }

    @ObjectFactory
    public Sort.Order fromJsonSortOrder(Sort.Order order){
        return new Sort.Order(order.getDirection(),fieldMapping.get(order.getProperty()));
    }

    public Set<String> getFilterKeys(){
        return fieldMapping.keySet();
    }
}
