package com.smartlibrary.dto.book;

import lombok.Data;

import java.math.BigDecimal;

/**
 * PATCH payload — all fields optional.
 */
@Data
public class BookPatchRequest {
    private String title;
    private String author;
    private String description;
    private Integer quantity;
    private BigDecimal price;
    private Integer publicationYear;
    private String imageUrl;
    private Long categoryId;
}
