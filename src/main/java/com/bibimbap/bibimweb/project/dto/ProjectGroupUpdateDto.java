package com.bibimbap.bibimweb.project.dto;

import com.bibimbap.bibimweb.member.dto.member.MemberResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectGroupUpdateDto {
    private Long id;
    private String period;
    private String teamName;
    private Long leaderId;
    private String content;
    @Builder.Default
    private List<Long> members = new ArrayList<>();
}
