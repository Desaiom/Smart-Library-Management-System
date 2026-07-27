package com.smartlibrary.repository;

import com.smartlibrary.entity.Borrow;
import com.smartlibrary.entity.BorrowStatus;
import com.smartlibrary.repository.projection.MonthlyBorrowStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    List<Borrow> findByUserId(Long userId);

    List<Borrow> findByUserIdAndStatus(Long userId, BorrowStatus status);

    long countByStatus(BorrowStatus status);

    long countByUserIdAndStatus(Long userId, BorrowStatus status);

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, BorrowStatus status);

    // Overdue: still borrowed but past due date (JPQL)
    @Query("SELECT b FROM Borrow b WHERE b.status = com.smartlibrary.entity.BorrowStatus.BORROWED " +
            "AND b.dueDate < :today")
    List<Borrow> findOverdue(@Param("today") LocalDate today);

    // Monthly borrow statistics for the dashboard chart (native, projection)
    @Query(value = """
            SELECT MONTH(borrow_date) AS month, YEAR(borrow_date) AS year, COUNT(*) AS total
            FROM borrows
            GROUP BY YEAR(borrow_date), MONTH(borrow_date)
            ORDER BY year, month
            """, nativeQuery = true)
    List<MonthlyBorrowStat> monthlyStatistics();
}
