package by.temniakov.testtask.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public enum City {
    VITEBSK("Vitebsk"),
    MINSK("Minsk");

    private final String city;
}
