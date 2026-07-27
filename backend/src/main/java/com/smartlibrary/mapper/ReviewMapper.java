package com.smartlibrary.mapper;

import com.smartlibrary.dto.review.ReviewResponse;
import com.smartlibrary.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .review(review.getReview())
                .createdAt(review.getCreatedAt())
                .userId(review.getUser() != null ? review.getUser().getId() : null)
                .userName(review.getUser() != null ? review.getUser().getName() : null)
                .bookId(review.getBook() != null ? review.getBook().getId() : null)
                .build();
    }
}
