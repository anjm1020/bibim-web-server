package com.bibimbap.bibimweb.member.dto.role;

import lombok.*;


@Getter @Setter @Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class RoleDto {
    private Long id;
    private String groupType;
    private Long groupId;
    private String groupName;
    private String role;
}
