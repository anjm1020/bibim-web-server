package com.bibimbap.bibimweb.dto.team.study;

import com.bibimbap.bibimweb.dto.team.TeamCreateDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyTeamCreateDto extends TeamCreateDto {
    private Map<Long,Integer> groupNumbers;
}
