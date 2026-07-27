package com.smartlibrary.dto.book;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 150)
    private String author;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^[0-9Xx-]{10,20}$", message = "ISBN must be 10-20 chars (digits, X, -)")
    private String isbn;

    @Size(max = 2000)
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @PositiveOrZero(message = "Price cannot be negative")
    private BigDecimal price;

    @Min(value = 1000, message = "Invalid publication year")
    @Max(value = 2100, message = "Invalid publication year")
    private Integer publicationYear;

    private String imageUrl;

    private Long categoryId;
}
