package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.OutOrderDto;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.OrderEvent;
import by.temniakov.testtask.store.repositories.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderEventService {
    private final OrderEventRepository orderEventRepository;


    public void saveAndFlush(Status status, OutOrderDto body) {
        OrderEvent orderEvent = OrderEvent.builder()
                .status(status)
                .orderJson(body)
                .build();

        orderEventRepository.saveAndFlush(orderEvent);
    }

    public Page<OrderEvent> findAllAmount(Integer amount) {
        return orderEventRepository.findAll(Pageable.ofSize(amount));
    }


    public void deleteAll(List<OrderEvent> events) {
        orderEventRepository.deleteAll(events);
    }
}
