package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.enums.City;
import by.temniakov.testtask.enums.Currency;
import by.temniakov.testtask.enums.Status;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseMapper {
    default Integer mapToInt(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        return null;
    }

    default String mapToString(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    default Double mapToDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        return null;
    }

    default Currency mapToCurrency(String  value) {
        if (value != null) {
            return Currency.valueOf(value) ;
        }
        return null;
    }

    default Status mapToStatus(String value){
        if (value != null) {
            return Status.valueOf(value) ;
        }
        return null;
    }

    default City mapToCity(String value){
        if (value != null) {
            return City.valueOf(value) ;
        }
        return null;
    }

    default String currencyToString(Currency value) {
        return value.name();
    }

    default String statusToString(Status value){
        return value.name();
    }

    default String cityToString(City value){
        return value.name();
    }
}
