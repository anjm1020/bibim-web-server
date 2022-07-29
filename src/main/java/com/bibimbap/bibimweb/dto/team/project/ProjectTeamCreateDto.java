package com.bibimbap.bibimweb.dto.team.project;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamCreateDto {
    private String groupName;
    private Long leaderId;
    private String period;

    private String content;

    @Builder.Default
    private List<Long> memberList = new ArrayList<>();
}
