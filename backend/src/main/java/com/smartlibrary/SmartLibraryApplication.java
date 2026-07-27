package com.smartlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application entry point.
 *
 * <p>Spring IoC container is bootstrapped here. {@link SpringBootApplication} is a
 * meta-annotation combining {@code @Configuration}, {@code @EnableAutoConfiguration}
 * and {@code @ComponentScan}, so every {@code @Component}, {@code @Service},
 * {@code @Repository}, {@code @Controller}/{@code @RestController} and
 * {@code @Configuration} bean below this package is auto-registered.</p>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
public class SmartLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartLibraryApplication.class, args);
    }
}
