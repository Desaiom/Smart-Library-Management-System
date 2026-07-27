package com.smartlibrary.mapper;

import com.smartlibrary.dto.borrow.BorrowResponse;
import com.smartlibrary.entity.Borrow;
import org.springframework.stereotype.Component;

@Component
public class BorrowMapper {

    public BorrowResponse toResponse(Borrow borrow) {
        return BorrowResponse.builder()
                .id(borrow.getId())
                .borrowDate(borrow.getBorrowDate())
                .dueDate(borrow.getDueDate())
                .returnDate(borrow.getReturnDate())
                .fine(borrow.getFine())
                .status(borrow.getStatus() != null ? borrow.getStatus().name() : null)
                .userId(borrow.getUser() != null ? borrow.getUser().getId() : null)
                .userName(borrow.getUser() != null ? borrow.getUser().getName() : null)
                .bookId(borrow.getBook() != null ? borrow.getBook().getId() : null)
                .bookTitle(borrow.getBook() != null ? borrow.getBook().getTitle() : null)
                .build();
    }
}
