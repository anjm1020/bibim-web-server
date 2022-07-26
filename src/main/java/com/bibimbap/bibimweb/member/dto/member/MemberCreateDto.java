package com.bibimbap.bibimweb.member.dto.member;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MemberCreateDto {
    private String name;
    private String studentId;
}
