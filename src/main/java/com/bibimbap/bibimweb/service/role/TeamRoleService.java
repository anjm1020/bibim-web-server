package com.bibimbap.bibimweb.service.role;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.RoleName;
import com.bibimbap.bibimweb.domain.role.team.ProjectRole;
import com.bibimbap.bibimweb.domain.role.team.StudyRole;
import com.bibimbap.bibimweb.domain.team.Team;
import com.bibimbap.bibimweb.repository.role.ProjectRoleRepository;
import com.bibimbap.bibimweb.repository.role.StudyRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamRoleService {
    private final ProjectRoleRepository projectRoleRepository;
    private final StudyRoleRepository studyRoleRepository;

    public void addProjectRole(Team team, Member member, RoleName roleName, String field) {
        ProjectRole saved = projectRoleRepository.save(ProjectRole.builder()
                .team(team)
                .member(member)
                .rollName(roleName.name())
                .field(field)
                .build());
        team.getMemberRoles().add(saved);
        member.getRoles().add(saved);
    }
    public void addStudyRole(Team team, Member member, RoleName roleName, Integer groupNumber) {
        StudyRole saved = studyRoleRepository.save(StudyRole.builder()
                .team(team)
                .member(member)
                .rollName(roleName.name())
                .groupNumber(groupNumber)
                .build());
        team.getMemberRoles().add(saved);
        member.getRoles().add(saved);
    }

    public void updateMemberOfRole(ProjectRole projectRole, Member newMember) {
        projectRole.getMember().getRoles().remove(projectRole);
        projectRole.setMember(newMember);
        projectRoleRepository.save(projectRole);
        newMember.getRoles().add(projectRole);
    }

    public void updateMemberOfRole(StudyRole studyRole, Member newMember) {
        studyRole.getMember().getRoles().remove(studyRole);
        studyRole.setMember(newMember);
        studyRoleRepository.save(studyRole);
        newMember.getRoles().add(studyRole);
    }

    public void deleteRole(ProjectRole projectRole) {
        projectRole.getMember().getRoles().remove(projectRole);
        projectRole.getTeam().getMemberRoles().remove(projectRole);
        projectRoleRepository.delete(projectRole);
    }
    public void deleteRole(StudyRole studyRole) {
        studyRole.getMember().getRoles().remove(studyRole);
        studyRole.getTeam().getMemberRoles().remove(studyRole);
        studyRoleRepository.delete(studyRole);
    }
}
