package com.smartlibrary.service;

import com.smartlibrary.dto.borrow.BorrowRequest;
import com.smartlibrary.dto.borrow.BorrowResponse;

import java.util.List;

public interface BorrowService {

    BorrowResponse borrow(BorrowRequest request);

    BorrowResponse returnBook(Long borrowId);

    List<BorrowResponse> myBorrows();

    List<BorrowResponse> byUser(Long userId);

    List<BorrowResponse> all();

    List<BorrowResponse> overdue();
}
