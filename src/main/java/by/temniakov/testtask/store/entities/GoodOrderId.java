package by.temniakov.testtask.store.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodOrderId implements Serializable{
    private Integer good;
    private Integer order;
}
