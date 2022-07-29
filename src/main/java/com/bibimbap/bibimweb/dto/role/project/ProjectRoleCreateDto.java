package com.bibimbap.bibimweb.dto.role.project;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRoleCreateDto {
    private String rollName;
    private Long memberId;
    private Long projectId;
}
