package com.bibimbap.bibimweb.review.repository;

import com.bibimbap.bibimweb.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
