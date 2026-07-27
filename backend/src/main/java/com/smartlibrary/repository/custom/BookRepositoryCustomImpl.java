package com.smartlibrary.repository.custom;

import com.smartlibrary.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Manual repository implementation using the {@link EntityManager} and the
 * persistence context directly. The Spring Data infrastructure automatically
 * wires this fragment into the {@code BookRepository} because of the
 * {@code Impl} naming convention.
 */
@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findTopBorrowed(int limit) {
        TypedQuery<Book> query = entityManager.createQuery(
                "SELECT b FROM Book b LEFT JOIN b.borrows br " +
                        "GROUP BY b ORDER BY COUNT(br) DESC", Book.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
