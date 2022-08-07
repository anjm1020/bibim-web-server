package com.bibimbap.bibimweb.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberResponseDto {
    private Long id;
    private String name;
    private String studentId;
    private String position;
}
