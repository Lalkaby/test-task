package by.temniakov.testtask.api.services.kafka;

import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.store.entities.OrderEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class OrderEventListener {
    private final ObjectMapper objectMapper;

    @KafkaListener(
            clientIdPrefix = "test-client",
            groupId="good-group",
            topics = "good",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenTestTask(ConsumerRecord<String,String> record){
        log.info("Written info: " + record.topic()+" "+ record.key());
        log.info("Written message: " + record.value());
        log.info("Written offset: " + record.offset());
        try {
            System.out.println(objectMapper.readValue(record.value(), OutGoodDto.class).getProducer());
        } catch (JsonProcessingException e) {
            System.out.println("Not deserialized");
        }
    }

    @KafkaListener(
            clientIdPrefix = "order-event-client",
            groupId="order-event-group",
            topics = "order-event",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenOrderEvents(ConsumerRecord<String,String> record){
        log.info("Listened order event:");
        try {
            var orderEvent = objectMapper.readValue(record.value(), OrderEvent.class);
            log.info("Order event uuid " + orderEvent.getUuid());
            log.info(orderEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
