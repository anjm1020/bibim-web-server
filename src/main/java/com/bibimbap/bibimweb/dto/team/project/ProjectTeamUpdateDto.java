package com.bibimbap.bibimweb.dto.team.project;

import com.bibimbap.bibimweb.dto.team.TeamUpdateDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamUpdateDto extends TeamUpdateDto {
    private String content;
}
