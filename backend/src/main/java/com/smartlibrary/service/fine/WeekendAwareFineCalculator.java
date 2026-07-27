package com.smartlibrary.service.fine;

import com.smartlibrary.config.LibraryProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Alternative fine strategy: weekends are not charged. Injected explicitly via
 * {@code @Qualifier("weekendAwareFineCalculator")} where a lenient policy is wanted.
 */
@Component("weekendAwareFineCalculator")
public class WeekendAwareFineCalculator implements FineCalculator {

    private final LibraryProperties properties;

    public WeekendAwareFineCalculator(LibraryProperties properties) {
        this.properties = properties;
    }

    @Override
    public BigDecimal calculate(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) {
            return BigDecimal.ZERO;
        }
        long chargeableDays = 0;
        LocalDate cursor = dueDate.plusDays(1);
        while (!cursor.isAfter(returnDate)) {
            DayOfWeek day = cursor.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                chargeableDays++;
            }
            cursor = cursor.plusDays(1);
        }
        return BigDecimal.valueOf(properties.getBorrow().getFinePerDay() * chargeableDays);
    }
}
