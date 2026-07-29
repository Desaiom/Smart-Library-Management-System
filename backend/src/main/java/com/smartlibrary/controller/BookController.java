package com.smartlibrary.controller;

import com.smartlibrary.dto.book.BookPatchRequest;
import com.smartlibrary.dto.book.BookRequest;
import com.smartlibrary.dto.book.BookResponse;
import com.smartlibrary.dto.common.ApiResponse;
import com.smartlibrary.dto.common.PageResponse;
import com.smartlibrary.repository.projection.BookSummary;
import com.smartlibrary.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * Book endpoints. Reads are public; writes require ADMIN/LIBRARIAN
 * (enforced centrally in {@code SecurityConfig}).
 */
@Tag(name = "Books", description = "Catalog management, search and filtering")
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "List books (paged & sorted)")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(ApiResponse.ok(bookService.list(page, size, sortBy, direction)));
    }

    @Operation(summary = "Keyword search across title/author/isbn")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.ok(bookService.search(keyword, page, size)));
    }

    @Operation(summary = "Dynamic multi-criteria filter (JPA Specifications)")
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> filter(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean available,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                bookService.filter(title, author, categoryId, minPrice, maxPrice, available, page, size)));
    }

    @Operation(summary = "Lightweight book summaries (projection)")
    @GetMapping("/summaries")
    public ResponseEntity<ApiResponse<List<BookSummary>>> summaries() {
        return ResponseEntity.ok(ApiResponse.ok(bookService.summaries()));
    }

    @Operation(summary = "Most borrowed books")
    @GetMapping("/top-borrowed")
    public ResponseEntity<ApiResponse<List<BookResponse>>> topBorrowed(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(ApiResponse.ok(bookService.topBorrowed(limit)));
    }

    @Operation(summary = "Get a single book")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(bookService.getById(id)));
    }

    @Operation(summary = "Create a book")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BookResponse>> create(

            @RequestPart("book") BookRequest request,

            @RequestPart(value = "image", required = false)
            MultipartFile image
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Book created",
                        bookService.create(request, image)
                ));
    }

    @Operation(summary = "Replace a book")
    @PutMapping(value="/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<ApiResponse<BookResponse>> update(

            @PathVariable Long id,

            @RequestPart("book") BookRequest request,

            @RequestPart(value = "image", required = false)
            MultipartFile image
    ) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Book updated",
                        bookService.update(id, request, image)
                )
        );
    }

    @Operation(summary = "Partially update a book")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BookResponse>> patch(
            @PathVariable Long id,
            @RequestPart("book") BookPatchRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Book updated",
                        bookService.patch(id, request, image)
                )
        );
    }

    @Operation(summary = "Delete a book")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Book deleted", null));
    }
}
