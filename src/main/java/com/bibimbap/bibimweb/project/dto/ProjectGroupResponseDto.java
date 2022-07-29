package com.bibimbap.bibimweb.project.dto;

import com.bibimbap.bibimweb.member.domain.Member;
import com.bibimbap.bibimweb.member.dto.member.MemberResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder @ToString
@NoArgsConstructor @AllArgsConstructor
public class ProjectGroupResponseDto {
    private Long id;
    private String period;
    private String teamName;
    private MemberResponseDto leader;
    private String content;
    @Builder.Default
    private List<MemberResponseDto> members = new ArrayList<>();
}
