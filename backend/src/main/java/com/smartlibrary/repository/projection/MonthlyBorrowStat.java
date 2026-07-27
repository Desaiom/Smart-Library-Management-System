package com.smartlibrary.repository.projection;

/**
 * Projection used for the monthly borrow-statistics dashboard chart.
 */
public interface MonthlyBorrowStat {
    Integer getMonth();
    Integer getYear();
    Long getTotal();
}
