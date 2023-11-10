package by.temniakov.testtask.api.mappers;

import by.temniakov.testtask.store.enums.Currency;

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

    default Currency mapToCurrency(Object value) {
        if (value instanceof Currency) {
            return (Currency) value;
        }
        return null;
    }
}
