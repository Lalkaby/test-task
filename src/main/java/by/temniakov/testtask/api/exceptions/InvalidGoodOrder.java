package by.temniakov.testtask.api.exceptions;


public record InvalidGoodOrder(Integer goodId, Integer withdrawAmount, int allowedAmount) {
}