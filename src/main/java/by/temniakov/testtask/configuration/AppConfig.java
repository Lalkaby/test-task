package by.temniakov.testtask.configuration;

import by.temniakov.testtask.api.mappers.factories.SortGoodFactory;
import by.temniakov.testtask.api.mappers.factories.SortOrderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableSpringDataWebSupport
@EnableScheduling
public class AppConfig {
    @Bean(name = "sortOrderFactory")
    public SortOrderFactory getSortOrderFactory(){
        return new SortOrderFactory();
    }
    @Bean(name = "sortGoodFactory")
    public SortGoodFactory getSortGoodFactory(){
        return new SortGoodFactory();
    }
}
