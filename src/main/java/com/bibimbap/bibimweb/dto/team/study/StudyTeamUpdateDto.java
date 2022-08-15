package com.bibimbap.bibimweb.dto.team.study;

import com.bibimbap.bibimweb.dto.team.TeamUpdateDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailCreateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudyTeamUpdateDto extends TeamUpdateDto {
    List<StudyDetailCreateDto> details;
}
