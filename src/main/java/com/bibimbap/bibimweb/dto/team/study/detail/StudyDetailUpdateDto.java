package com.bibimbap.bibimweb.dto.team.study.detail;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyDetailUpdateDto {
    private Long detail_id;
    private Integer week;
    private String content;
    private List<AttendanceManageDto> attendances;
}
