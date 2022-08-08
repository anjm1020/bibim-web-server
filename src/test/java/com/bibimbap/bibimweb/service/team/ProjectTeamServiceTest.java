package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import com.bibimbap.bibimweb.domain.team.tag.TeamTag;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamUpdateDto;
import com.bibimbap.bibimweb.repository.team.ProjectTeamRepository;
import com.bibimbap.bibimweb.repository.team.tag.TeamTagRepository;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
class ProjectTeamServiceTest {

    @Autowired
    ProjectTeamService projectTeamService;
    @Autowired
    TagService tagService;
    @Autowired
    ProjectTeamRepository projectTeamRepository;
    @Autowired
    TeamTagRepository teamTagRepository;

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

    @Test
    @DisplayName("팀-태그 테스트")
    void teamTagTest() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());

        List<String> tagList = new ArrayList<>();
        tagList.add("Tag1");
        tagList.add("MyTag");

        System.out.println("=== TEAM SAVE ===");
        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName("team1")
                .leaderId(memberA.getId())
                .content("Project Team")
                .members(memberList)
                .tags(tagList)
                .build();
        ProjectTeamResponseDto saved = projectTeamService.createProjectTeam(dto);

        System.out.println("=== TEAM_TAG FIND ===");
        List<TeamTag> allByTeamId = teamTagRepository.findAllByTeamId(saved.getId());
        System.out.println("FIND ALL BY TEAM");
        for (TeamTag teamTag : allByTeamId) {
            System.out.println(teamTag);
        }

        System.out.println("=== TAG FIND BY TEAM ===");
        ProjectTeam team = projectTeamRepository.findById(saved.getId()).get();
        System.out.println(team.getTags().size());
        for (TeamTag tag : team.getTags()) {
            System.out.println(tag);
        }

        tagList.add("Tag2");
        tagList.remove("Tag1");
        ProjectTeamResponseDto projectTeamResponseDto = projectTeamService.updateProjectTeam(ProjectTeamUpdateDto.builder()
                .id(saved.getId())
                .groupName(saved.getGroupName())
                .members(saved.getMembers().stream().map(m -> m.getId()).collect(Collectors.toList()))
                .leaderId(saved.getLeader().getId())
                .content(saved.getContent())
                .tags(tagList)
                .build());

        System.out.println("UPDATE TAG : RESPONSE DTO");
        System.out.println(projectTeamResponseDto);

        System.out.println("=== AFTER DELETE ENTIRE TAG ===");
        tagService.deleteAllTeamTag(team.getId());
        System.out.println(team.getTags().size());
        for (TeamTag tag : team.getTags()) {
            System.out.println(tag);
        }
    }
}