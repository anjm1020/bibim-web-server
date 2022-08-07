package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.bibimbap.bibimweb.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProjectTeamServiceTest {

    @Autowired
    ProjectTeamService projectTeamService;

    @Autowired
    MemberManager memberManager;

    @Test
    @DisplayName("팀 생성 테스트")
    void createTeam() {

        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");

        List<Long> memberList = new ArrayList<>();

        memberList.add(memberB.getId());

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName("team1")
                .leaderId(memberA.getId())
                .content("Project Team")
                .build();

        ProjectTeamResponseDto saved = projectTeamService.createProjectTeam(dto);
    }

}