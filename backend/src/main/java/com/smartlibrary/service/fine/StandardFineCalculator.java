package com.smartlibrary.service.fine;

import com.smartlibrary.config.LibraryProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Default fine strategy: flat per-day fine for every day past the due date.
 * Marked {@code @Primary} so it is chosen when no {@code @Qualifier} is given.
 */
@Primary
@Component("standardFineCalculator")
public class StandardFineCalculator implements FineCalculator {

    private final LibraryProperties properties;

    public StandardFineCalculator(LibraryProperties properties) {
        this.properties = properties;
    }

    @Override
    public BigDecimal calculate(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) {
            return BigDecimal.ZERO;
        }
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        return BigDecimal.valueOf(properties.getBorrow().getFinePerDay() * daysLate);
    }
}
