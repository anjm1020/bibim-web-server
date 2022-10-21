package com.bibimbap.bibimweb.dto.member.role;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberDto {
    private Long memberId;
    private String position;
}
