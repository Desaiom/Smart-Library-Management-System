package com.smartlibrary.controller;

import com.smartlibrary.dto.borrow.BorrowRequest;
import com.smartlibrary.dto.borrow.BorrowResponse;
import com.smartlibrary.dto.common.ApiResponse;
import com.smartlibrary.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Borrow / return endpoints. Method-level {@code @PreAuthorize} restricts the
 * administrative listings to staff, demonstrating fine-grained authorization.
 */
@Tag(name = "Borrows", description = "Borrow and return workflow")
@RestController
@RequestMapping("/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @Operation(summary = "Borrow a book")
    @PostMapping
    public ResponseEntity<ApiResponse<BorrowResponse>> borrow(@Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Book borrowed", borrowService.borrow(request)));
    }

    @Operation(summary = "Return a borrowed book")
    @PutMapping("/{id}/return")
    public ResponseEntity<ApiResponse<BorrowResponse>> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Book returned", borrowService.returnBook(id)));
    }

    @Operation(summary = "My borrow history")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> myBorrows() {
        return ResponseEntity.ok(ApiResponse.ok(borrowService.myBorrows()));
    }

    @Operation(summary = "Borrow history for a user (staff only)")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(borrowService.byUser(userId)));
    }

    @Operation(summary = "All borrows (staff only)")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> all() {
        return ResponseEntity.ok(ApiResponse.ok(borrowService.all()));
    }

    @Operation(summary = "Overdue borrows (staff only)")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> overdue() {
        return ResponseEntity.ok(ApiResponse.ok(borrowService.overdue()));
    }
}
