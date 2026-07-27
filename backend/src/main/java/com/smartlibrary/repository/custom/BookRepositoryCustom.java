package com.smartlibrary.repository.custom;

import com.smartlibrary.entity.Book;

import java.util.List;

/**
 * Custom repository fragment implemented manually with the JPA
 * {@code EntityManager} / persistence context.
 */
public interface BookRepositoryCustom {
    List<Book> findTopBorrowed(int limit);
}
