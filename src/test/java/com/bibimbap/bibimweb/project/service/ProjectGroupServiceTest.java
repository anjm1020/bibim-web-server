package com.bibimbap.bibimweb.project.service;

import com.bibimbap.bibimweb.member.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.member.service.MemberService;
import com.bibimbap.bibimweb.project.dto.ProjectGroupCreateDto;
import com.bibimbap.bibimweb.project.dto.ProjectGroupResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Transactional
@SpringBootTest
class ProjectGroupServiceTest {

    @Autowired
    ProjectGroupService projectGroupService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("프로젝트 생성")
    void createProjectGroup() {
        // create member
        Long id = memberService.createMember(MemberCreateDto.builder()
                .name("jaemin")
                .studentId("1111")
                .build()).getId();

        List<Long> list = new ArrayList<>();
        list.add(id);
        ProjectGroupResponseDto result = projectGroupService.createProjectGroup(ProjectGroupCreateDto.builder()
                .teamName("testPG")
                .content("lorem ipsum..")
                .leaderId(id)
                .period("2022")
                .members(list)
                .build());
        Assertions.assertThat(result.getMembers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("프로젝트 리스트")
    void getProjectGroupList() {

    }
}