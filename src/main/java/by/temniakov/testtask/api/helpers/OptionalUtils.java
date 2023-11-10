package by.temniakov.testtask.api.helpers;

import java.util.Optional;

public class OptionalUtils {
    public static <T> T fromOptional(Optional<T> optional) {
        return optional.orElse( null );
    }
    public static <T> Optional<T> toOptional(T value) {
        return Optional.ofNullable(value);
    }
}
