package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.team.Team;
import com.bibimbap.bibimweb.domain.team.review.Review;
import com.bibimbap.bibimweb.dto.review.ReviewCreateDto;
import com.bibimbap.bibimweb.dto.review.ReviewResponseDto;
import com.bibimbap.bibimweb.dto.review.ReviewUpdateDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.team.TeamRepository;
import com.bibimbap.bibimweb.repository.team.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ModelMapper mapper = new ModelMapper();

    public ReviewResponseDto createReview(ReviewCreateDto dto) {
        Member member = memberRepository.findById(dto.getMemberId()).get();
        Team team = teamRepository.findById(dto.getTeamId()).get();

        Review saved = reviewRepository.save(Review.builder()
                .content(dto.getContent())
                .member(member)
                .team(team)
                .build());
        return mapper.map(saved, ReviewResponseDto.class);
    }

    public ReviewResponseDto getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        return mapper.map(review, ReviewResponseDto.class);
    }
    public List<ReviewResponseDto> getReviewList(Pageable pageable, Integer period) {
        if (period == null) {
            return reviewRepository.findAll(pageable).stream()
                    .map(review -> mapper.map(review, ReviewResponseDto.class))
                    .collect(Collectors.toList());
        }
        return reviewRepository.findAllByTeamPeriod(period, pageable).stream()
                .map(review -> mapper.map(review,ReviewResponseDto.class))
                .collect(Collectors.toList());
    }

    public ReviewResponseDto updateReview(ReviewUpdateDto dto) {
        Member member = memberRepository.findById(dto.getMemberId()).get();
        Team team = teamRepository.findById(dto.getTeamId()).get();
        Review saved = reviewRepository.save(Review.builder()
                .id(dto.getId())
                .team(team)
                .member(member)
                .content(dto.getContent())
                .build());
        return mapper.map(saved, ReviewResponseDto.class);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
