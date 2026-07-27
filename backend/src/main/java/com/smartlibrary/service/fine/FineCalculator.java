package com.smartlibrary.service.fine;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Strategy for computing overdue fines. Two implementations exist; the
 * {@code @Primary} one is injected by default, and a specific one can be
 * selected with {@code @Qualifier}.
 */
public interface FineCalculator {

    /**
     * @param dueDate    the date the book was due
     * @param returnDate the actual return date (or today if not yet returned)
     * @return the fine amount, never negative
     */
    BigDecimal calculate(LocalDate dueDate, LocalDate returnDate);
}
