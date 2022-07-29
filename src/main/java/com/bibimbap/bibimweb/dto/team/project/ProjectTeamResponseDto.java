package com.bibimbap.bibimweb.dto.team.project;

import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamResponseDto {
    private Long id;
    private String groupName;
    private MemberResponseDto leaderId;
    private String period;

    private String content;

    @Builder.Default
    private List<MemberResponseDto> memberId = new ArrayList<>();
}
