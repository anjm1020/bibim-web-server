package com.bibimbap.bibimweb.dto.team.study;

import com.bibimbap.bibimweb.dto.team.TeamUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class StudyTeamUpdateDto extends TeamUpdateDto {
    private Map<Long,Integer> groupNumbers;
}
