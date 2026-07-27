package com.smartlibrary.controller;

import com.smartlibrary.dto.common.ApiResponse;
import com.smartlibrary.dto.review.ReviewRequest;
import com.smartlibrary.dto.review.ReviewResponse;
import com.smartlibrary.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reviews", description = "Book ratings and reviews")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "List reviews for a book (public)")
    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> byBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(ApiResponse.ok(reviewService.byBook(bookId)));
    }

    @Operation(summary = "Add a review for a book")
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> add(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Review added", reviewService.addReview(request)));
    }

    @Operation(summary = "Delete a review")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.ok("Review deleted", null));
    }
}
