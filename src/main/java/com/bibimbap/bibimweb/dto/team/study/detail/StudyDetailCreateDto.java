package com.bibimbap.bibimweb.dto.team.study.detail;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyDetailCreateDto {
    private Integer week;
    private String detail;
}
