package com.smartlibrary.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private long totalBooks;
    private long availableBooks;
    private long borrowedBooks;
    private long totalUsers;
    private long totalCategories;
    private long activeBorrowings;
    private long overdueBorrowings;
    private List<MonthlyStat> monthlyBorrowStatistics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyStat {
        private int year;
        private int month;
        private long total;
    }
}
