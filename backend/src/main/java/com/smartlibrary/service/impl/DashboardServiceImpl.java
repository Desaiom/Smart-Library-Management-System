package com.smartlibrary.service.impl;

import com.smartlibrary.dto.dashboard.DashboardResponse;
import com.smartlibrary.entity.BorrowStatus;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.BorrowRepository;
import com.smartlibrary.repository.CategoryRepository;
import com.smartlibrary.repository.UserRepository;
import com.smartlibrary.repository.projection.MonthlyBorrowStat;
import com.smartlibrary.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Aggregates statistics for the admin/librarian dashboard.
 * Demonstrates combining derived counts, aggregate JPQL and native projections.
 */
@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BorrowRepository borrowRepository;

    public DashboardServiceImpl(BookRepository bookRepository,
                                UserRepository userRepository,
                                CategoryRepository categoryRepository,
                                BorrowRepository borrowRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.borrowRepository = borrowRepository;
    }

    @Override
    public DashboardResponse getStatistics() {
        long totalBooks = bookRepository.count();
        long availableBooks = bookRepository.totalAvailableCopies();
        long borrowed = borrowRepository.countByStatus(BorrowStatus.BORROWED);

        List<DashboardResponse.MonthlyStat> monthly = borrowRepository.monthlyStatistics().stream()
                .map(this::toMonthlyStat)
                .toList();

        return DashboardResponse.builder()
                .totalBooks(totalBooks)
                .availableBooks(availableBooks)
                .borrowedBooks(borrowed)
                .totalUsers(userRepository.count())
                .totalCategories(categoryRepository.count())
                .activeBorrowings(borrowed)
                .overdueBorrowings(borrowRepository.findOverdue(LocalDate.now()).size())
                .monthlyBorrowStatistics(monthly)
                .build();
    }

    private DashboardResponse.MonthlyStat toMonthlyStat(MonthlyBorrowStat stat) {
        return DashboardResponse.MonthlyStat.builder()
                .year(stat.getYear())
                .month(stat.getMonth())
                .total(stat.getTotal())
                .build();
    }
}
