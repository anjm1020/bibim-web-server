package com.bibimbap.bibimweb.dto.team.project;

import com.bibimbap.bibimweb.dto.team.TeamCreateDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamCreateDto extends TeamCreateDto {
    private String content;
}
