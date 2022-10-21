package com.bibimbap.bibimweb.dto.team.project;

import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.team.TeamResponseDto;
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
public class ProjectTeamResponseDto extends TeamResponseDto {
    private String content;
}
