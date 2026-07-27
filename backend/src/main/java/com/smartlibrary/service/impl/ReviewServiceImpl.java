package com.smartlibrary.service.impl;

import com.smartlibrary.dto.review.ReviewRequest;
import com.smartlibrary.dto.review.ReviewResponse;
import com.smartlibrary.entity.Book;
import com.smartlibrary.entity.Review;
import com.smartlibrary.entity.User;
import com.smartlibrary.exception.DuplicateResourceException;
import com.smartlibrary.exception.InvalidOperationException;
import com.smartlibrary.exception.ResourceNotFoundException;
import com.smartlibrary.mapper.ReviewMapper;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.ReviewRepository;
import com.smartlibrary.security.SecurityUtils;
import com.smartlibrary.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final ReviewMapper reviewMapper;
    private final SecurityUtils securityUtils;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             BookRepository bookRepository,
                             ReviewMapper reviewMapper,
                             SecurityUtils securityUtils) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.reviewMapper = reviewMapper;
        this.securityUtils = securityUtils;
    }

    @Override
    @Transactional
    public ReviewResponse addReview(ReviewRequest request) {
        User current = securityUtils.getCurrentUser();
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", request.getBookId()));

        reviewRepository.findByUserIdAndBookId(current.getId(), book.getId()).ifPresent(r -> {
            throw new DuplicateResourceException("You have already reviewed this book");
        });

        Review review = Review.builder()
                .user(current)
                .book(book)
                .rating(request.getRating())
                .review(request.getReview())
                .build();
        Review saved = reviewRepository.save(review);
        log.info("User id={} reviewed book id={} rating={}", current.getId(), book.getId(), request.getRating());
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        User current = securityUtils.getCurrentUser();
        if (!securityUtils.isStaff() && !review.getUser().getId().equals(current.getId())) {
            throw new InvalidOperationException("You can only delete your own reviews");
        }
        reviewRepository.delete(review);
        log.info("Deleted review id={}", reviewId);
    }

    @Override
    public List<ReviewResponse> byBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book", "id", bookId);
        }
        return reviewRepository.findByBookIdOrdered(bookId).stream().map(reviewMapper::toResponse).toList();
    }
}
