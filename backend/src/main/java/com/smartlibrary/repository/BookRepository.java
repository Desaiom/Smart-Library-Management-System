package com.smartlibrary.repository;

import com.smartlibrary.entity.Book;
import com.smartlibrary.repository.custom.BookRepositoryCustom;
import com.smartlibrary.repository.projection.BookSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Book repository.
 *
 * <p>Demonstrates: derived query methods, JPQL ({@code @Query}), native SQL,
 * projections, pagination/sorting via {@link Pageable}, and dynamic filtering
 * through {@link JpaSpecificationExecutor}.</p>
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book>, BookRepositoryCustom {

    // ---- Derived query methods ----
    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByAuthorIgnoreCase(String author);

    List<Book> findByCategoryId(Long categoryId);

    Page<Book> findByAvailableQuantityGreaterThan(int qty, Pageable pageable);

    // ---- JPQL search across multiple fields ----
    @Query("""
            SELECT b FROM Book b
            WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Book> search(@Param("keyword") String keyword, Pageable pageable);

    // ---- Interface projection query ----
    @Query("SELECT b.id AS id, b.title AS title, b.author AS author, " +
            "b.isbn AS isbn, b.availableQuantity AS availableQuantity FROM Book b")
    List<BookSummary> findAllSummaries();

    // ---- Native query ----
    @Query(value = "SELECT * FROM books WHERE available_quantity > 0 ORDER BY id DESC LIMIT :limit",
            nativeQuery = true)
    List<Book> findLatestAvailable(@Param("limit") int limit);

    // ---- Aggregations for the dashboard ----
    @Query("SELECT COALESCE(SUM(b.availableQuantity), 0) FROM Book b")
    long totalAvailableCopies();
}
