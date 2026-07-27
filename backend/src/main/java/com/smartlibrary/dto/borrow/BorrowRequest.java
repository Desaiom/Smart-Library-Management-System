package com.smartlibrary.dto.borrow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorrowRequest {

    @NotNull(message = "bookId is required")
    private Long bookId;

    /** Optional; ADMIN/LIBRARIAN may borrow on behalf of a user. */
    private Long userId;
}
