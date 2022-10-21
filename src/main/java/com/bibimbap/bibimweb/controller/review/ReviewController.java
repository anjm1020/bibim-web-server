package com.bibimbap.bibimweb.controller.review;

import com.bibimbap.bibimweb.dto.review.ReviewCreateDto;
import com.bibimbap.bibimweb.dto.review.ReviewResponseDto;
import com.bibimbap.bibimweb.dto.review.ReviewUpdateDto;
import com.bibimbap.bibimweb.service.team.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/reviews", produces = "application/json; charset=UTF8")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/")
    ReviewResponseDto createReview(@RequestBody ReviewCreateDto review) {
        return reviewService.createReview(review);
    }

    @GetMapping("/{reviewId}")
    ReviewResponseDto getReviewById(@PathVariable Long reviewId) {
        return reviewService.getReview(reviewId);
    }

    @GetMapping("/")
    List<ReviewResponseDto> getReviewList(Pageable pageable, @RequestParam Integer period) {
        return reviewService.getReviewList(pageable, period);
    }

    @PutMapping("/")
    ReviewResponseDto updateReview(@RequestParam ReviewUpdateDto review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{reviewId}")
    ResponseEntity deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
