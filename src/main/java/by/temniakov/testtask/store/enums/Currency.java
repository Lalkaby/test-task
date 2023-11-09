package by.temniakov.testtask.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public enum Currency {
    BYN("BYN"),
    RUB("RUB"),
    USD("USD");

    private final String currency;
}
