package by.temniakov.testtask.store.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodOrderEntityId implements Serializable{
    private Integer good;
    private Integer order;
}
