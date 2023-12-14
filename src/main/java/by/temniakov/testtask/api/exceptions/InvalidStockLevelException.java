package by.temniakov.testtask.api.exceptions;

import lombok.Getter;

@Getter
public class InvalidStockLevelException extends RuntimeException  {
    private final Integer goodId;
    private final Integer currentLevel;


    public InvalidStockLevelException(String message, Integer goodId, Integer currentLevel) {
        super(message);
        this.goodId = goodId;
        this.currentLevel = currentLevel;
    }
}
