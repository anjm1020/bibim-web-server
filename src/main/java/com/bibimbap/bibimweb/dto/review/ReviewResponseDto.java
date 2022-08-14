package com.bibimbap.bibimweb.dto.review;

import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.team.TeamResponseDto;
import lombok.*;

@Getter @Setter @Builder @ToString
@NoArgsConstructor @AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String content;
    private MemberResponseDto member;
    private TeamResponseDto team;
}
