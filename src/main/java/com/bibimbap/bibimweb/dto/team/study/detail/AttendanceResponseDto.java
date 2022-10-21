package com.bibimbap.bibimweb.dto.team.study.detail;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDto {
    private String memberName;
    private Integer groupNumber;
    private Boolean isAttend;
}
