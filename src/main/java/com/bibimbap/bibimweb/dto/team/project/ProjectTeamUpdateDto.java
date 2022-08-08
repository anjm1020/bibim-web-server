package com.bibimbap.bibimweb.dto.team.project;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamUpdateDto {
    private Long id;
    private String groupName;
    private Long leaderId;

    private String content;

    @Builder.Default
    private List<Long> members = new ArrayList<>();
    @Builder.Default
    private List<String> tags = new ArrayList<>();
}
