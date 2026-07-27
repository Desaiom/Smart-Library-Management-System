package com.smartlibrary.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Type-safe binding of {@code library.*} properties from application.yml.
 * Demonstrates {@code @ConfigurationProperties} (Configuration Properties concept).
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "library")
public class LibraryProperties {

    private Jwt jwt = new Jwt();
    private Borrow borrow = new Borrow();
    private Storage storage = new Storage();

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private long expirationMs = 86_400_000L;
    }

    @Getter
    @Setter
    public static class Borrow {
        private int loanPeriodDays = 14;
        private double finePerDay = 5.0;
        private int maxActiveBorrows = 5;
    }

    @Getter
    @Setter
    public static class Storage {
        private String uploadDir = "uploads";
    }
}
