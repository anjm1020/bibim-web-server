package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.ProjectRole;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
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

    private final TagService tagService;
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
        ProjectTeam saved = projectTeamRepository.save(newTeam);

        // project Role setting
        ProjectRole leaderRole = projectRoleRepository.save(ProjectRole.builder()
                .team(saved)
                .member(leader)
                .rollName("LEADER")
                .build());
        leader.getRoles().add(leaderRole);
        saved.getRoles().add(leaderRole);

        // 멤버 리스트 잡고 저장해주기
        for (Long memberId : dto.getMembers()) {
            Member member = memberRepository.findById(memberId).get();
            ProjectRole memberRole = projectRoleRepository.save(ProjectRole.builder()
                    .team(saved)
                    .member(member)
                    .rollName("MEMBER")
                    .build());
            member.getRoles().add(memberRole);
            saved.getRoles().add(memberRole);
        }
        // Tag Setting
        tagService.saveTags(saved.getId(), dto.getTags());
        return makeResponseDto(saved);
    }

    public List<ProjectTeamResponseDto> getProjectTeamList(Pageable pageable) {
        return projectTeamRepository.findAll(pageable).stream()
                .map(o -> makeResponseDto(o))
                .collect(Collectors.toList());
    }

    public ProjectTeamResponseDto getProjectTeamById(Long teamId) {
        return makeResponseDto(projectTeamRepository.findById(teamId).get());
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
            projectTeam.getRoles().add(savedLeaderRole);
            leader.getRoles().add(savedLeaderRole);
        } else {
            ProjectRole projectRole = leaderRole.get();
            projectRole.getMember().getRoles().remove(projectRole);
            projectRole.setMember(leader);
            savedLeaderRole = projectRoleRepository.save(projectRole);
            leader.getRoles().add(savedLeaderRole);
        }
        memberRepository.save(leader);

        // member mapping
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
                curr.getRoles().add(savedMemberRole);
                projectTeam.getRoles().add(savedMemberRole);
                memberRepository.save(curr);
            }
        }

        List<ProjectRole> tmp = projectRoleRepository.findAllByTeamId(dto.getId());

        // find member to delete
        List<ProjectRole> teamMembers = projectRoleRepository.findAllByTeamIdAndRollName(dto.getId(), "MEMBER");
        for (ProjectRole pr : teamMembers) {
            if (!members.stream().anyMatch(id -> pr.getMember().getId() == id)) {
                pr.getMember().getRoles().remove(pr);
                pr.getTeam().getRoles().remove(pr);
                projectRoleRepository.deleteById(pr.getId());
            }
        }

        // tag update
        tagService.updateTags(projectTeam.getId(), dto.getTags());
        ProjectTeam saved = projectTeamRepository.save(projectTeam);
        return makeResponseDto(saved);
    }

    public void deleteProjectTeam(Long teamId) {
        projectTeamRepository.deleteById(teamId);
    }

    public ProjectTeamResponseDto makeResponseDto(ProjectTeam projectTeam) {
        ProjectTeamResponseDto res = mapper.map(projectTeam, ProjectTeamResponseDto.class);
        res.setMembers(projectTeam.getRoles().stream()
                .filter(r -> r.getRollName().equals("MEMBER"))
                .map(r -> mapper.map(r.getMember(), MemberResponseDto.class))
                .collect(Collectors.toList()));
        return res;
    }
}
