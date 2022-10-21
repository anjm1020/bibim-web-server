package com.bibimbap.bibimweb.service.review;

import com.bibimbap.bibimweb.domain.team.review.Review;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.review.ReviewCreateDto;
import com.bibimbap.bibimweb.dto.review.ReviewResponseDto;
import com.bibimbap.bibimweb.dto.review.ReviewUpdateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.bibimbap.bibimweb.repository.team.review.ReviewRepository;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.bibimbap.bibimweb.service.member.MemberService;
import com.bibimbap.bibimweb.service.team.ProjectTeamService;
import com.bibimbap.bibimweb.service.team.ReviewService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MemberManager memberManager;

    @Autowired
    MemberService memberService;
    @Autowired
    ProjectTeamService projectTeamService;

    @Test
    @DisplayName("리뷰 통합 테스트")
    void createReview() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "11");
        MemberResponseDto memberB = memberManager.createMember("memberB", "22");
        ProjectTeamResponseDto team = projectTeamService.createProjectTeam(ProjectTeamCreateDto.builder()
                .content("123")
                .leaderId(memberA.getId())
                .groupName("team1")
                .build());

        String content = "It's the Review";
        ReviewResponseDto saved = reviewService.createReview(ReviewCreateDto.builder()
                .content(content)
                .memberId(memberA.getId())
                .teamId(team.getId())
                .build());

        ReviewResponseDto saved2 = reviewService.createReview(ReviewCreateDto.builder()
                .content(content)
                .memberId(memberB.getId())
                .teamId(team.getId())
                .build());

        Review findReview = reviewRepository.findById(saved.getId()).get();
        assertThat(findReview.getContent()).isEqualTo(content);
        assertThat(findReview.getMember().getId()).isEqualTo(memberA.getId());
        assertThat(findReview.getTeam().getId()).isEqualTo(team.getId());

        List<ReviewResponseDto> reviewList = reviewService.getReviewList(PageRequest.of(0, 10), null);
        assertThat(reviewList.size()).isEqualTo(2);
        assertThat(reviewList.stream().anyMatch(review -> review.getId().equals(saved.getId()))).isTrue();
        assertThat(reviewList.stream().anyMatch(review -> review.getId().equals(saved2.getId()))).isTrue();
        List<ReviewResponseDto> reviewList2 = reviewService.getReviewList(PageRequest.of(0, 10), 2021);
        assertThat(reviewList2.size()).isEqualTo(0);
        List<ReviewResponseDto> reviewList3 = reviewService.getReviewList(PageRequest.of(0, 10), 2022);
        assertThat(reviewList3.size()).isEqualTo(2);
        assertThat(reviewList3.stream().anyMatch(review -> review.getId().equals(saved.getId()))).isTrue();
        assertThat(reviewList3.stream().anyMatch(review -> review.getId().equals(saved2.getId()))).isTrue();

        String updatedContent = "UpdatedContent";
        System.out.println(reviewService.updateReview(ReviewUpdateDto.builder()
                .id(saved.getId())
                .content(updatedContent)
                .memberId(saved.getMember().getId())
                .teamId(saved.getTeam().getId())
                .build()));
        Review findUpdated = reviewRepository.findById(saved.getId()).get();
        assertThat(findUpdated.getContent()).isEqualTo(updatedContent);
        assertThat(findUpdated.getMember().getId()).isEqualTo(saved.getMember().getId());
        assertThat(findUpdated.getTeam().getId()).isEqualTo(saved.getTeam().getId());

        reviewService.deleteReview(saved.getId());
        assertThat(reviewRepository.findById(saved.getId()).isPresent()).isFalse();
    }
}
