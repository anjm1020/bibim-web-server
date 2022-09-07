package com.bibimbap.bibimweb.dto.member.role;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HonorMemberDto {
    private Long memberId;
    private String groupName;
}
