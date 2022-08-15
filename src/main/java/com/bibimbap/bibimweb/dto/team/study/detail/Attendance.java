package com.bibimbap.bibimweb.dto.team.study.detail;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    private String memberName;
    private Boolean isAttend;
}
