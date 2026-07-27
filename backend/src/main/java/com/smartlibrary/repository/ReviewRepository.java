package com.smartlibrary.repository;

import com.smartlibrary.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Uses the @NamedQuery declared on the Review entity.
    List<Review> findByBookIdOrdered(@Param("bookId") Long bookId);

    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double averageRating(@Param("bookId") Long bookId);
}
