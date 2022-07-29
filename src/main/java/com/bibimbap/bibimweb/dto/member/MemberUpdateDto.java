package com.bibimbap.bibimweb.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
    private Long id;
    private String name;
    private String studentId;
}
