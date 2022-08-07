package com.bibimbap.bibimweb.dto.team.project;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamCreateDto {
    private String groupName;
    private Long leaderId;

    private String content;

    @Builder.Default
    private List<Long> members = new ArrayList<>();
}
