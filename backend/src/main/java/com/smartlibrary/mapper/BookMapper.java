package com.smartlibrary.mapper;

import com.smartlibrary.dto.book.BookRequest;
import com.smartlibrary.dto.book.BookResponse;
import com.smartlibrary.entity.Book;
import com.smartlibrary.entity.Category;
import org.springframework.stereotype.Component;

/**
 * Maps between {@link Book} entities and their DTO representations.
 *
 * <p>Hand-written mapping keeps the entity/DTO boundary explicit and avoids
 * leaking lazy JPA associations into the web layer.</p>
 */
@Component
public class BookMapper {

    public Book toEntity(BookRequest request, Category category) {
        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .availableQuantity(request.getQuantity())
                .price(request.getPrice())
                .publicationYear(request.getPublicationYear())
                .category(category)
                .build();
    }

    public BookResponse toResponse(Book book, Double averageRating) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .quantity(book.getQuantity())
                .availableQuantity(book.getAvailableQuantity())
                .price(book.getPrice())
                .publicationYear(book.getPublicationYear())
                .imageUrl(book.getImageUrl())
                .categoryId(book.getCategory() != null ? book.getCategory().getId() : null)
                .categoryName(book.getCategory() != null ? book.getCategory().getName() : null)
                .averageRating(averageRating)
                .build();
    }
}
