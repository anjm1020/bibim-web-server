package com.bibimbap.bibimweb.dto.member;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberCreateDto {
    private String name;
    private String studentId;
}
