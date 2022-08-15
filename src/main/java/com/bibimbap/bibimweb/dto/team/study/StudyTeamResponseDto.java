package com.bibimbap.bibimweb.dto.team.study;

import com.bibimbap.bibimweb.dto.team.TeamResponseDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyTeamResponseDto extends TeamResponseDto {
    List<StudyDetailResponseDto> details;
}
