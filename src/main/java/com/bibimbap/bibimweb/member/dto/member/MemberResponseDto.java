package com.bibimbap.bibimweb.member.dto.member;

import com.bibimbap.bibimweb.member.dto.role.RoleResponseDto;
import lombok.*;

import java.util.List;

@Builder
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;
    private String studentId;
    private String attendance;
    private List<RoleResponseDto> roles;
}
