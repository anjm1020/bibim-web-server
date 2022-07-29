package com.bibimbap.bibimweb.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.bibimbap.bibimweb.member.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.member.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.project.dto.ProjectGroupCreateDto;
import com.bibimbap.bibimweb.project.dto.ProjectGroupResponseDto;
import com.bibimbap.bibimweb.project.dto.ProjectGroupUpdateDto;
import com.bibimbap.bibimweb.util.exception.OutOfRangeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProjectGroupControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    ModelMapper modelMapper = new ModelMapper();

    MemberResponseDto createMember(String name, String id) throws Exception {
        return objectMapper.readValue(mockMvc.perform(post("/members/")
                        .content(objectMapper.writeValueAsString(MemberCreateDto.builder()
                                .name(name)
                                .studentId(id)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), MemberResponseDto.class);
    }

    @Test
    @DisplayName("프로젝트 그룹 생성")
    void createGroup() throws Exception {
        MemberResponseDto memberA = createMember("memberA", "111");
        MemberResponseDto memberB = createMember("memberB", "222");

        List<Long> members = new ArrayList<>();
        members.add(memberA.getId());
        members.add(memberB.getId());

        mockMvc.perform(post("/project/groups/")
                        .content(objectMapper.writeValueAsString(ProjectGroupCreateDto.builder()
                                .teamName("testTeam")
                                .leaderId(memberA.getId())
                                .content("content")
                                .period("2022")
                                .members(members)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("프로젝트 리스트")
    void getListGroup() throws Exception {
        MemberResponseDto memberA = createMember("memberA", "111");
        MemberResponseDto memberB = createMember("memberB", "222");

        List<Long> members = new ArrayList<>();
        members.add(memberA.getId());
        members.add(memberB.getId());

        mockMvc.perform(post("/project/groups/")
                        .content(objectMapper.writeValueAsString(ProjectGroupCreateDto.builder()
                                .teamName("testTeam")
                                .leaderId(memberA.getId())
                                .content("content")
                                .period("2022")
                                .members(members)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/project/groups/")
                        .content(objectMapper.writeValueAsString(ProjectGroupCreateDto.builder()
                                .teamName("testTeam")
                                .leaderId(memberA.getId())
                                .content("content")
                                .period("2022")
                                .members(members)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/project/groups/")
                        .queryParam("page", "0")
                        .queryParam("size", "5"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(get("/project/groups/")
                        .queryParam("page", "1")
                        .queryParam("size", "5"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getClass()
                        .isAssignableFrom(OutOfRangeException.class)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        .getMessage().equals(OutOfRangeException.PAGE.getMessage())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("기간으로 프로젝트 리스트 불러오기")
    void getListByPeriod() throws Exception {
        MemberResponseDto memberA = createMember("memberA", "111");
        MemberResponseDto memberB = createMember("memberB", "222");

        List<Long> members = new ArrayList<>();
        members.add(memberA.getId());
        members.add(memberB.getId());

        mockMvc.perform(post("/project/groups/")
                        .content(objectMapper.writeValueAsString(ProjectGroupCreateDto.builder()
                                .teamName("team1")
                                .leaderId(memberA.getId())
                                .content("content1")
                                .period("11")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(post("/project/groups/")
                        .content(objectMapper.writeValueAsString(ProjectGroupCreateDto.builder()
                                .teamName("team2")
                                .leaderId(memberA.getId())
                                .content("content2")
                                .period("11")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(post("/project/groups/")
                        .content(objectMapper.writeValueAsString(ProjectGroupCreateDto.builder()
                                .teamName("team3")
                                .leaderId(memberA.getId())
                                .content("content3")
                                .period("12")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(get("/project/groups/")
                        .queryParam("period", "11"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("프로젝트 그룹 수정 테스트")
    void updateProjectGroup() throws Exception {
        MemberResponseDto memberA = createMember("memberA", "111");
        MemberResponseDto memberB = createMember("memberB", "222");

        List<Long> members = new ArrayList<>();
        members.add(memberB.getId());

        String result = mockMvc.perform(post("/project/groups/")
                        .content(objectMapper.writeValueAsString(ProjectGroupCreateDto.builder()
                                .teamName("team1")
                                .leaderId(memberA.getId())
                                .content("content1")
                                .period("11")
                                .members(members)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        members = new ArrayList<>();
        members.add(memberA.getId());
        ProjectGroupResponseDto res = objectMapper.readValue(result, ProjectGroupResponseDto.class);
        ProjectGroupUpdateDto dto = ProjectGroupUpdateDto.builder()
                .id(res.getId())
                .teamName("teamTest")
                .content(res.getContent())
                .leaderId(memberB.getId())
                .period("11")
                .members(members)
                .build();

        mockMvc.perform(put("/project/groups/")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("프로젝트 그룹 삭제 테스트")
    void deleteProjectGroup() throws Exception {
        MemberResponseDto memberA = createMember("memberA", "111");
        MemberResponseDto memberB = createMember("memberB", "222");

        List<Long> members = new ArrayList<>();
        members.add(memberB.getId());

        String result = mockMvc.perform(post("/project/groups/")
                        .content(objectMapper.writeValueAsString(ProjectGroupCreateDto.builder()
                                .teamName("team1")
                                .leaderId(memberA.getId())
                                .content("content1")
                                .period("11")
                                .members(members)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ProjectGroupResponseDto res = objectMapper.readValue(result, ProjectGroupResponseDto.class);

        mockMvc.perform(delete("/project/groups/" + res.getId()))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(get("/project/groups/" + res.getId()))
                .andExpect(status().isNotFound());
    }

}