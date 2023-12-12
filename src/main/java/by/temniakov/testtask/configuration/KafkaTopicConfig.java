package by.temniakov.testtask.configuration;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicTestTask() {
        return new NewTopic("testtask", 1, (short) 1);
    }

    @Bean
    public NewTopic topicOrderEvent() {
        return new NewTopic("order-event", 1, (short) 1);
    }

}