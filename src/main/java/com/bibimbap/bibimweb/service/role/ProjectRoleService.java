package com.bibimbap.bibimweb.service.role;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.team.ProjectRole;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import com.bibimbap.bibimweb.dto.role.project.ProjectRoleCreateDto;
import com.bibimbap.bibimweb.dto.role.project.ProjectRoleDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.ProjectRoleRepository;
import com.bibimbap.bibimweb.repository.team.ProjectTeamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRoleService {

    private final MemberRepository memberRepository;
    private final ProjectTeamRepository projectTeamRepository;
    private final ProjectRoleRepository projectRoleRepository;

    private final ModelMapper mapper = new ModelMapper();

    public ProjectRoleDto createProjectRole(ProjectRoleCreateDto dto) {

        Member member = memberRepository.findById(dto.getMemberId()).get();
        ProjectTeam projectTeam = projectTeamRepository.findById(dto.getProjectId()).get();
        ProjectRole newRole = projectRoleRepository.save(ProjectRole.builder()
                .rollName(dto.getRollName())
                .member(member)
                .team(projectTeam)
                .build());
        List<Role> roles = member.getRoles();
        roles.add(newRole);
        member.setRoles(roles);
        return mapper.map(newRole, ProjectRoleDto.class);
    }

    public ProjectRoleDto getProjectRoleById(Long roleId) {
        return mapper.map(projectRoleRepository.findById(roleId).get(), ProjectRoleDto.class);
    }


}
