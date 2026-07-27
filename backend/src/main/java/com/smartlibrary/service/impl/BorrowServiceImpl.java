package com.smartlibrary.service.impl;

import com.smartlibrary.config.LibraryProperties;
import com.smartlibrary.dto.borrow.BorrowRequest;
import com.smartlibrary.dto.borrow.BorrowResponse;
import com.smartlibrary.entity.Book;
import com.smartlibrary.entity.Borrow;
import com.smartlibrary.entity.BorrowStatus;
import com.smartlibrary.entity.User;
import com.smartlibrary.exception.BookNotAvailableException;
import com.smartlibrary.exception.InvalidOperationException;
import com.smartlibrary.exception.ResourceNotFoundException;
import com.smartlibrary.mapper.BorrowMapper;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.BorrowRepository;
import com.smartlibrary.repository.UserRepository;
import com.smartlibrary.security.SecurityUtils;
import com.smartlibrary.service.BorrowService;
import com.smartlibrary.service.fine.FineCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Borrow / return workflow.
 *
 * <p>Demonstrates transactional integrity (decrementing stock and creating a
 * loan atomically), the fine {@code Strategy} bean injected via
 * {@code @Qualifier}, business rule enforcement, and current-user resolution.</p>
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowMapper borrowMapper;
    private final SecurityUtils securityUtils;
    private final LibraryProperties properties;
    private final FineCalculator fineCalculator;

    public BorrowServiceImpl(BorrowRepository borrowRepository,
                             BookRepository bookRepository,
                             UserRepository userRepository,
                             BorrowMapper borrowMapper,
                             SecurityUtils securityUtils,
                             LibraryProperties properties,
                             @Qualifier("standardFineCalculator") FineCalculator fineCalculator) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowMapper = borrowMapper;
        this.securityUtils = securityUtils;
        this.properties = properties;
        this.fineCalculator = fineCalculator;
    }

    @Override
    @Transactional
    public BorrowResponse borrow(BorrowRequest request) {
        User current = securityUtils.getCurrentUser();
        User borrower = resolveBorrower(request, current);

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", request.getBookId()));

        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() <= 0) {
            throw new BookNotAvailableException("No copies available for: " + book.getTitle());
        }

        long active = borrowRepository.countByUserIdAndStatus(borrower.getId(), BorrowStatus.BORROWED);
        if (active >= properties.getBorrow().getMaxActiveBorrows()) {
            throw new InvalidOperationException(
                    "Active borrow limit reached (" + properties.getBorrow().getMaxActiveBorrows() + ")");
        }
        if (borrowRepository.existsByUserIdAndBookIdAndStatus(borrower.getId(), book.getId(), BorrowStatus.BORROWED)) {
            throw new InvalidOperationException("This book is already borrowed by the user");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);

        LocalDate today = LocalDate.now();
        Borrow borrow = Borrow.builder()
                .user(borrower)
                .book(book)
                .borrowDate(today)
                .dueDate(today.plusDays(properties.getBorrow().getLoanPeriodDays()))
                .status(BorrowStatus.BORROWED)
                .fine(BigDecimal.ZERO)
                .build();

        Borrow saved = borrowRepository.save(borrow);
        log.info("User id={} borrowed book id={} (borrow id={})", borrower.getId(), book.getId(), saved.getId());
        return borrowMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BorrowResponse returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow", "id", borrowId));

        if (borrow.getStatus() == BorrowStatus.RETURNED) {
            throw new InvalidOperationException("Book already returned");
        }

        User current = securityUtils.getCurrentUser();
        if (!securityUtils.isStaff() && !borrow.getUser().getId().equals(current.getId())) {
            throw new InvalidOperationException("You can only return your own borrowed books");
        }

        LocalDate today = LocalDate.now();
        borrow.setReturnDate(today);
        borrow.setStatus(BorrowStatus.RETURNED);
        borrow.setFine(fineCalculator.calculate(borrow.getDueDate(), today));

        Book book = borrow.getBook();
        book.setAvailableQuantity(Math.min(book.getQuantity(), book.getAvailableQuantity() + 1));

        log.info("Borrow id={} returned, fine={}", borrowId, borrow.getFine());
        return borrowMapper.toResponse(borrow);
    }

    @Override
    public List<BorrowResponse> myBorrows() {
        User current = securityUtils.getCurrentUser();
        return borrowRepository.findByUserId(current.getId()).stream().map(borrowMapper::toResponse).toList();
    }

    @Override
    public List<BorrowResponse> byUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return borrowRepository.findByUserId(userId).stream().map(borrowMapper::toResponse).toList();
    }

    @Override
    public List<BorrowResponse> all() {
        return borrowRepository.findAll().stream().map(borrowMapper::toResponse).toList();
    }

    @Override
    public List<BorrowResponse> overdue() {
        return borrowRepository.findOverdue(LocalDate.now()).stream().map(borrowMapper::toResponse).toList();
    }

    private User resolveBorrower(BorrowRequest request, User current) {
        if (request.getUserId() != null && securityUtils.isStaff()) {
            return userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
        }
        return current;
    }
}
