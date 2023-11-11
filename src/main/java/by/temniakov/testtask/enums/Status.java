package by.temniakov.testtask.enums;

public enum Status {
    ACTIVE,
    CANCELLED,
    COMPLETED;

    public static Status[] getValues(){
        return values();
    }
}
