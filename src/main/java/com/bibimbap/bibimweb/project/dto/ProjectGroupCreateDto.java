package com.bibimbap.bibimweb.project.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ProjectGroupCreateDto {
    private String period;
    private String teamName;
    private Long leaderId;
    private String content;

    @Builder.Default
    private List<Long> members = new ArrayList<>();
}