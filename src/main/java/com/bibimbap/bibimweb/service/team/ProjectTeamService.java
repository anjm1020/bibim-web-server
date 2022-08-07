package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.ProjectRole;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamUpdateDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.ProjectRoleRepository;
import com.bibimbap.bibimweb.repository.team.ProjectTeamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectTeamService {

    private final ProjectTeamRepository projectTeamRepository;
    private final MemberRepository memberRepository;
    private final ProjectRoleRepository projectRoleRepository;

    private final ModelMapper mapper = new ModelMapper();

    public boolean isValidPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long count = projectTeamRepository.count();
        return 0 <= pageNumber && pageNumber <= ((count - 1) / pageSize);
    }

    public boolean isExistTeam(Long teamId) {
        return projectTeamRepository.existsById(teamId);
    }

    public ProjectTeamResponseDto createProjectTeam(ProjectTeamCreateDto dto) {
        Member leader = memberRepository.findById(dto.getLeaderId()).get();
        ProjectTeam newTeam = ProjectTeam.builder()
                .groupName(dto.getGroupName())
                .leader(leader)
                .content(dto.getContent())
                .build();

        newTeam.setPeriod(String.valueOf(LocalDate.now().getYear()));
        newTeam.setMembers(dto.getMembers().stream()
                .map(o -> memberRepository.findById(o).get())
                .collect(Collectors.toList()));
        ProjectTeam saved = projectTeamRepository.save(newTeam);

        projectRoleRepository.save(ProjectRole.builder()
                .team(saved)
                .member(leader)
                .rollName("LEADER")
                .build());
        saved.getMembers().stream()
                .forEach(m -> projectRoleRepository.save(ProjectRole.builder()
                        .team(saved)
                        .member(m)
                        .rollName("MEMBER")
                        .build()));
        return mapper.map(saved, ProjectTeamResponseDto.class);
    }

    public List<ProjectTeamResponseDto> getProjectTeamList(Pageable pageable) {
        return projectTeamRepository.findAll(pageable).stream()
                .map(o -> mapper.map(o, ProjectTeamResponseDto.class))
                .collect(Collectors.toList());
    }

    public ProjectTeamResponseDto getProjectTeamById(Long teamId) {
        return mapper.map(projectTeamRepository.findById(teamId).get(), ProjectTeamResponseDto.class);
    }

    public ProjectTeamResponseDto updateProjectTeam(ProjectTeamUpdateDto dto) {
        ProjectTeam projectTeam = projectTeamRepository.findById(dto.getId()).get();
        projectTeam.setContent(dto.getContent());
        // group name
        projectTeam.setGroupName(dto.getGroupName());
        // leader mapping
        Member leader = memberRepository.findById(dto.getLeaderId()).get();
        projectTeam.setLeader(leader);
        Optional<ProjectRole> leaderRole = projectRoleRepository.findByTeamIdAndRollName(dto.getId(), "LEADER");
        ProjectRole savedLeaderRole;
        if (leaderRole.isEmpty()) {
            savedLeaderRole = projectRoleRepository.save(ProjectRole.builder()
                    .team(projectTeam)
                    .member(leader)
                    .rollName("LEADER")
                    .build());
        } else {
            ProjectRole projectRole = leaderRole.get();
            projectRole.setMember(leader);
            savedLeaderRole = projectRoleRepository.save(projectRole);
        }
        List<Role> roles = leader.getRoles();
        roles.add(savedLeaderRole);
        leader.setRoles(roles);
        memberRepository.save(leader);

        // member mapping
        List<Member> newMembers = new ArrayList<>();
        List<Long> members = dto.getMembers();
        for (Long id : members) {
            Member curr = memberRepository.findById(id).get();
            Optional<ProjectRole> memberRole = projectRoleRepository.findByTeamIdAndRollNameAndMemberId(dto.getId(), "MEMBER", id);
            ProjectRole savedMemberRole;
            if (memberRole.isEmpty()) {
                // 신규 멤버
                savedMemberRole = projectRoleRepository.save(ProjectRole.builder()
                        .team(projectTeam)
                        .member(curr)
                        .rollName("MEMBER")
                        .build());
                roles = curr.getRoles();
                roles.add(savedMemberRole);
                curr.setRoles(roles);
                memberRepository.save(curr);
            }
            newMembers.add(curr);
        }
        projectTeam.setMembers(newMembers);

        // find member to delete
        List<ProjectRole> teamMembers = projectRoleRepository.findAllByTeamIdAndRollName(dto.getId(), "MEMBER");
        for (ProjectRole m : teamMembers) {
            Optional<Long> found = members.stream()
                    .filter(o -> m.getId() == o)
                    .findAny();
            if (found.isEmpty()) {
                projectRoleRepository.deleteById(m.getId());
            }
        }
        ProjectTeam saved = projectTeamRepository.save(projectTeam);
        return mapper.map(saved, ProjectTeamResponseDto.class);
    }

    public void deleteProjectTeam(Long teamId) {
        projectTeamRepository.deleteById(teamId);
    }

}
