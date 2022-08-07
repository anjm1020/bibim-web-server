package com.bibimbap.bibimweb.dto.role.project;

import com.bibimbap.bibimweb.dto.member.MemberDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamDto;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRoleDto {
    private Long id;
    private String rollName;
    private MemberDto member;
    private ProjectTeamDto projectTeam;
}
