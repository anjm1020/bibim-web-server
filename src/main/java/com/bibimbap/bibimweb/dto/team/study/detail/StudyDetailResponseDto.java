package com.bibimbap.bibimweb.dto.team.study.detail;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyDetailResponseDto {
    private Long id;
    private String content;
    private Integer week;
    private List<AttendanceResponseDto> attendances;
}
