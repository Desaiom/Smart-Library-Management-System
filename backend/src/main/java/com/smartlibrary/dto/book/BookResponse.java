package com.smartlibrary.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Integer quantity;
    private Integer availableQuantity;
    private BigDecimal price;
    private Integer publicationYear;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private Double averageRating;
}
