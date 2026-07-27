package com.smartlibrary.service;

import com.smartlibrary.dto.review.ReviewRequest;
import com.smartlibrary.dto.review.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse addReview(ReviewRequest request);

    void deleteReview(Long reviewId);

    List<ReviewResponse> byBook(Long bookId);
}
