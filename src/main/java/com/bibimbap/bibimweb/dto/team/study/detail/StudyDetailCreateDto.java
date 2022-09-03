package com.bibimbap.bibimweb.dto.team.study.detail;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyDetailCreateDto {
    private Long teamId;
    private Integer week;
    private String content;
    // Attendance 를 넣어야해
    private List<AttendanceManageDto> attendances;
    // copy that
}
