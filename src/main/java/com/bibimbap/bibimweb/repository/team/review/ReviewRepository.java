package com.bibimbap.bibimweb.repository.team.review;

import com.bibimbap.bibimweb.domain.team.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByTeamPeriod(Integer period, Pageable pageable);

}
