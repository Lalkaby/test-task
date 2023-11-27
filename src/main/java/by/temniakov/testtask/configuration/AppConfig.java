package by.temniakov.testtask.configuration;

import by.temniakov.testtask.api.mappers.factories.SortGoodFactory;
import by.temniakov.testtask.api.mappers.factories.SortOrderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport
public class AppConfig {
    @Bean(name = "exampleMatcherWithIgnoreIdPath")
    public ExampleMatcher getExampleMatcherWithIgnoreIdPath(){
        return ExampleMatcher.matching().withIgnorePaths("id");
    }

    @Bean(name = "sortOrderFactory")
    public SortOrderFactory getSortOrderFactory(){
        return new SortOrderFactory();
    }
    @Bean(name = "sortGoodFactory")
    public SortGoodFactory getSortGoodFactory(){
        return new SortGoodFactory();
    }
}
