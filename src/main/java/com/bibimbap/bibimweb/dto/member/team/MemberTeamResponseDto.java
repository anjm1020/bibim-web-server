package com.bibimbap.bibimweb.dto.member.team;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberTeamResponseDto {
    private Long id;
    private String name;
}
