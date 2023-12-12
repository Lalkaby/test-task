package by.temniakov.testtask.api.services.kafka;

import by.temniakov.testtask.api.services.OrderEventService;
import by.temniakov.testtask.store.entities.OrderEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final OrderEventService orderEventService;
    private final ObjectMapper objectMapper;

    private  final String ORDER_EVENT_TOPIC = "order-event";

    @Scheduled(fixedRateString = "${delays.order-event.send}")
    public void sendOrderEvent(){
        List<OrderEvent> events = orderEventService.findAllAmount(100).toList();

        for (var event:events) {
            try {
                kafkaTemplate.send(ORDER_EVENT_TOPIC, objectMapper.writeValueAsString(event));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        orderEventService.deleteAll(events);
    }
}
