package com.smartlibrary.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Application-level bean definitions.
 *
 * <p>Demonstrates {@code @Configuration} + {@code @Bean} factory methods and a
 * prototype-scoped bean ({@link RequestScopedCounter}).</p>
 */
@Configuration
public class AppConfig {

    /**
     * ModelMapper bean used across the mapper layer for entity <-> DTO mapping.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
        return mapper;
    }

    /**
     * Prototype-scoped bean — a new instance is created on every injection point
     * / lookup, demonstrating bean scopes.
     */
    @Bean
    @Scope("prototype")
    public RequestScopedCounter requestScopedCounter() {
        return new RequestScopedCounter();
    }

    /** Simple stateful helper to make the prototype scope observable. */
    public static class RequestScopedCounter {
        private int count = 0;

        public int increment() {
            return ++count;
        }
    }
}
