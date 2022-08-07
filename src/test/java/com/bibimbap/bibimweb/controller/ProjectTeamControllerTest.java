package com.bibimbap.bibimweb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProjectTeamControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    ModelMapper modelMapper = new ModelMapper();

    final String HOR = "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$";

    @Test
    @DisplayName("멤버 생성 -> 팀 생성 테스트")
    void createMemberAndTeam() throws Exception {

        System.out.println(HOR);
        System.out.println("MEMBER1 CREATE");
        String memberAStr = mockMvc.perform(post("/members/")
                        .content(objectMapper.writeValueAsString(MemberCreateDto.builder()
                                .name("memberA")
                                .studentId("1111")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MemberResponseDto memberA = objectMapper.readValue(memberAStr, MemberResponseDto.class);

        System.out.println(HOR);
        System.out.println("MEMBER2 CREATE");
        String memberBStr = mockMvc.perform(post("/members/")
                        .content(objectMapper.writeValueAsString(MemberCreateDto.builder()
                                .name("memberB")
                                .studentId("2222")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MemberResponseDto memberB = objectMapper.readValue(memberBStr, MemberResponseDto.class);

        System.out.println(HOR);
        System.out.println("MEMBER3 CREATE");
        String memberCStr = mockMvc.perform(post("/members/")
                        .content(objectMapper.writeValueAsString(MemberCreateDto.builder()
                                .name("memberC")
                                .studentId("3333")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MemberResponseDto memberC = objectMapper.readValue(memberCStr, MemberResponseDto.class);

        List<Long> memberList1 = new ArrayList<>();
        memberList1.add(memberB.getId());
        memberList1.add(memberC.getId());
        List<Long> memberList2 = new ArrayList<>();
        memberList2.add(memberA.getId());
        memberList2.add(memberC.getId());

        System.out.println(HOR);
        System.out.println("TEAM1 CREATE");
        // team 만들기
        String team1Str = mockMvc.perform(post("/teams/project/")
                        .content(objectMapper.writeValueAsString(ProjectTeamCreateDto.builder()
                                .groupName("team1")
                                .content("It's Project Team 1")
                                .leaderId(memberA.getId())
                                .members(memberList1)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        ProjectTeamResponseDto team1 = objectMapper.readValue(team1Str, ProjectTeamResponseDto.class);

        System.out.println(HOR);
        System.out.println("TEAM2 CREATE");
        String team2Str = mockMvc.perform(post("/teams/project/")
                        .content(objectMapper.writeValueAsString(ProjectTeamCreateDto.builder()
                                .groupName("team2")
                                .content("It's Project Team 2")
                                .leaderId(memberB.getId())
                                .members(memberList2)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        ProjectTeamResponseDto team2 = objectMapper.readValue(team2Str, ProjectTeamResponseDto.class);


    }

}
