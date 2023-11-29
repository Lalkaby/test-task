package by.temniakov.testtask.store.entities;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class GoodOrderId implements Serializable{
    private Integer good;
    private Integer order;
}
