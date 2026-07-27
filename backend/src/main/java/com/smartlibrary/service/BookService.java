package com.smartlibrary.service;

import com.smartlibrary.dto.book.BookPatchRequest;
import com.smartlibrary.dto.book.BookRequest;
import com.smartlibrary.dto.book.BookResponse;
import com.smartlibrary.dto.common.PageResponse;
import com.smartlibrary.repository.projection.BookSummary;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {

    BookResponse create(BookRequest request);

    BookResponse update(Long id, BookRequest request);

    BookResponse patch(Long id, BookPatchRequest request);

    void delete(Long id);

    BookResponse getById(Long id);

    PageResponse<BookResponse> list(int page, int size, String sortBy, String direction);

    PageResponse<BookResponse> search(String keyword, int page, int size);

    /** Dynamic multi-criteria filtering backed by JPA Specifications. */
    PageResponse<BookResponse> filter(String title, String author, Long categoryId,
                                      BigDecimal minPrice, BigDecimal maxPrice,
                                      Boolean available, int page, int size);

    List<BookSummary> summaries();

    List<BookResponse> topBorrowed(int limit);
}
