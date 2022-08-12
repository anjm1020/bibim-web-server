package com.bibimbap.bibimweb.dto.team.project;

import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamResponseDto {
    private Long id;
    private String groupName;
    private MemberResponseDto leader;
    private Integer period;

    private String content;

    @Builder.Default
    private List<MemberResponseDto> members = new ArrayList<>();
    @Builder.Default
    private List<String> tags = new ArrayList<>();
}
