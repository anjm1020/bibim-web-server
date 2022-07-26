package com.bibimbap.bibimweb.member.dto.role;

import lombok.*;

@Getter @Setter @Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class RoleResponseDto {
    private Long id;
    private String groupName;
    private String role;
}
