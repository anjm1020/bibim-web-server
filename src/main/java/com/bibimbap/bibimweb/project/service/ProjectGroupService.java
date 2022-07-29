package com.bibimbap.bibimweb.project.service;

import com.bibimbap.bibimweb.member.domain.Member;
import com.bibimbap.bibimweb.member.domain.Role;
import com.bibimbap.bibimweb.member.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.member.dto.role.RoleResponseDto;
import com.bibimbap.bibimweb.member.dto.role.RoleUpdateDto;
import com.bibimbap.bibimweb.member.repository.RoleRepository;
import com.bibimbap.bibimweb.member.service.MemberService;
import com.bibimbap.bibimweb.member.service.RoleService;
import com.bibimbap.bibimweb.project.domain.ProjectGroup;
import com.bibimbap.bibimweb.project.dto.ProjectGroupCreateDto;
import com.bibimbap.bibimweb.project.dto.ProjectGroupResponseDto;
import com.bibimbap.bibimweb.project.dto.ProjectGroupUpdateDto;
import com.bibimbap.bibimweb.project.repository.ProjectGroupRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectGroupService {

    private final ProjectGroupRepository projectGroupRepository;
    private final MemberService memberService;
    private final RoleService roleService;

    private final RoleRepository roleRepository;

    private final String LEADER = "팀장";
    private final String MEMBER = "팀원";
    private final String PROJECT = "프로젝트";

    private final ModelMapper mapper = new ModelMapper();

    public boolean isValidPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long count = projectGroupRepository.count();
        return 0 <= pageNumber && pageNumber <= ((count - 1) / pageSize);
    }

    public boolean isExistGroup(Long id) {
        return projectGroupRepository.existsById(id);
    }

    public ProjectGroupResponseDto createProjectGroup(ProjectGroupCreateDto dto) {

        ProjectGroup newGroup = mapper.map(dto, ProjectGroup.class);
        List<Member> memberList = memberService.getMemberListById(dto.getMembers());

        newGroup.setMembers(memberList);
        newGroup.setId(null);
        ProjectGroup saved = projectGroupRepository.save(newGroup);

        for (Member member : memberList) {
            memberService.updateRoleOfMember(member, RoleUpdateDto.builder()
                    .role(MEMBER)
                    .groupName(dto.getTeamName())
                    .groupId(saved.getId())
                    .groupType(PROJECT)
                    .status(1)
                    .build());
        }

        MemberResponseDto leader = memberService.getMemberById(dto.getLeaderId());
        memberService.updateRoleOfMember(mapper.map(leader, Member.class), RoleUpdateDto.builder()
                .role(LEADER)
                .groupName(dto.getTeamName())
                .groupId(saved.getId())
                .groupType(PROJECT)
                .status(1)
                .build());
        return convertToResponseDto(saved);
    }

    public List<ProjectGroupResponseDto> getProjectGroupList(Pageable pageable) {
        return projectGroupRepository.findAll(pageable).getContent()
                .stream()
                .map(o -> convertToResponseDto(o))
                .collect(Collectors.toList());
    }

    public List<ProjectGroupResponseDto> getProjectGroupByPeriod(String period) {
        return projectGroupRepository.findAllByPeriod(period).stream()
                .map(o -> convertToResponseDto(o))
                .collect(Collectors.toList());
    }

    public ProjectGroupResponseDto getProjectGroupById(Long id) {
        return convertToResponseDto(projectGroupRepository.findById(id).get());
    }

    public ProjectGroupResponseDto updateProjectGroup(ProjectGroupUpdateDto dto) {

        ProjectGroup updated = mapper.map(dto, ProjectGroup.class);
        ProjectGroup original = projectGroupRepository.findById(dto.getId()).get();
        // leader
        Member originalLeader = original.getLeader();
        MemberResponseDto newLeader = memberService.getMemberById(dto.getLeaderId());
        if (originalLeader.getId() != newLeader.getId()) {
            // 기존 리더의 롤 빼주기
            Long deleteId = roleService.getLeaderRoleOfGroup(dto.getId(), PROJECT, LEADER);
            memberService.updateRoleOfMember(originalLeader, RoleUpdateDto.builder()
                    .id(deleteId)
                    .status(2)
                    .build());
            // 새롭게 롤 만들어서 새로운 리더에게 추가해주기
            memberService.updateRoleOfMember(mapper.map(newLeader, Member.class), RoleUpdateDto.builder()
                    .role(LEADER)
                    .groupName(dto.getTeamName())
                    .groupId(dto.getId())
                    .groupType(PROJECT)
                    .status(1)
                    .build());
        }
        updated.setLeader(mapper.map(newLeader, Member.class));
        // member
        List<Member> originalMembers = original.getMembers();
        List<Member> newMembers = memberService.getMemberListById(dto.getMembers());
        for (Member member : newMembers) {
            Optional<Member> found = originalMembers.stream()
                    .filter(o -> o.getId() == member.getId())
                    .findAny();
            if (!found.isPresent()) {
                // 새롭게 추가되는 멤버 : role 추가
                memberService.updateRoleOfMember(member, RoleUpdateDto.builder()
                        .status(1)
                        .groupName(dto.getTeamName())
                        .groupId(dto.getId())
                        .groupType(PROJECT)
                        .role(MEMBER)
                        .build());
            }
        }

        for (Member member : originalMembers) {
            Optional<Member> found = newMembers.stream()
                    .filter(o -> member.getId() == o.getId())
                    .findAny();
            if (!found.isPresent()) {
                // 제거되어야 하는 멤버 : role 제거
                Long deleteId = roleService.getMemberRoleOfGroup(member.getId(),dto.getId(),PROJECT,MEMBER);
                memberService.updateRoleOfMember(member, RoleUpdateDto.builder()
                        .id(deleteId)
                        .status(2)
                        .build());
            }
        }

        updated.setMembers(newMembers);
        return convertToResponseDto(projectGroupRepository.save(updated));
    }

    public void deleteProjectGroup(Long id) {
        // leader role 지워주기
        ProjectGroup target = projectGroupRepository.findById(id).get();
        Long leaderRoleId = roleService.getLeaderRoleOfGroup(target.getId(), PROJECT, LEADER);
        memberService.updateRoleOfMember(target.getLeader(), RoleUpdateDto.builder()
                .id(leaderRoleId)
                .status(2)
                .build());

        // member role 지워주기
        for (Member member : target.getMembers()) {
            Long memberRoleId = roleService.getMemberRoleOfGroup(member.getId(), target.getId(), PROJECT, MEMBER);
            memberService.updateRoleOfMember(member, RoleUpdateDto.builder()
                    .id(memberRoleId)
                    .status(2)
                    .build());
        }
        // 그리고 pg 지우면 되나
        projectGroupRepository.deleteById(id);
    }

    private ProjectGroupResponseDto convertToResponseDto(ProjectGroup group) {
        ProjectGroupResponseDto res = mapper.map(group, ProjectGroupResponseDto.class);
        res.setLeader(memberService.addRolesToDto(
                mapper.map(group.getLeader(), MemberResponseDto.class)
        ));

        List<MemberResponseDto> members = new ArrayList<>();
        for (Member member : group.getMembers()) {
            members.add(memberService.addRolesToDto(
                    mapper.map(member, MemberResponseDto.class)
            ));
        }
        res.setMembers(members);
        return res;
    }

}
