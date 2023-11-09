package by.temniakov.testtask.store.repositories.specifications;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
