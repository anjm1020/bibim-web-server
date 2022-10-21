package com.bibimbap.bibimweb.dto.team.study.detail;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceManageDto {
    private Long memberId;
    private Integer week;
    private boolean isAttend;
}
