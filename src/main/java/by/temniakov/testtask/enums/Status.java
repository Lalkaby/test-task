package by.temniakov.testtask.enums;

public enum Status {
    DRAFT,
    ACTIVE,
    CANCELLED,
    COMPLETED;

    public static Status[] getValues(){
        return values();
    }
}
