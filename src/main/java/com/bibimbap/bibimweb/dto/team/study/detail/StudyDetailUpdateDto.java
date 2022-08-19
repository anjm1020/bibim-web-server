package com.bibimbap.bibimweb.dto.team.study.detail;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyDetailUpdateDto {
    private Long detailId;
    private Integer week;
    private String detail;
}
