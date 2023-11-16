package by.temniakov.testtask.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;

@Configuration
public class AppConfig {
    @Bean(name = "exampleMatcherWithIgnoreIdPath")
    public ExampleMatcher getExampleMatcherWithIgnoreIdPath(){
        return ExampleMatcher.matching().withIgnorePaths("id");
    }
}
