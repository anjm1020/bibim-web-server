package com.bibimbap.bibimweb.service.role;

import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.role.project.ProjectRoleCreateDto;
import com.bibimbap.bibimweb.dto.role.project.ProjectRoleDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.bibimbap.bibimweb.service.member.MemberService;
import com.bibimbap.bibimweb.service.team.ProjectTeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProjectRoleServiceTest {

    @Autowired
    ProjectRoleService projectRoleService;

    @Autowired
    MemberService memberService;

    @Autowired
    ProjectTeamService projectTeamService;
    @Autowired
    MemberManager memberManager;

    @Test
    @DisplayName("역할 생성 테스트")
    void createRole() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "11");

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName("team1")
                .leaderId(memberA.getId())
                .content("content")
                .build();

        ProjectTeamResponseDto team1 = projectTeamService.createProjectTeam(dto);

        ProjectRoleDto role = projectRoleService.createProjectRole(ProjectRoleCreateDto.builder()
                .rollName("팀장")
                .memberId(memberA.getId())
                .projectId(team1.getId())
                .build());

        System.out.println(role);

        memberService.getMemberById(memberA.getId());
    }
}