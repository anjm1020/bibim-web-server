package com.bibimbap.bibimweb.controller.team;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.study.detail.AttendanceManageDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailCreateDto;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class StudyTeamControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberManager memberManager;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("test")
    void test() throws JsonProcessingException {
        Map<Long, Integer> map = new HashMap<>();
        map.put(1L, 1);
        map.put(2L, 1);
        map.put(3L, 1);
        System.out.println(objectMapper.writeValueAsString(map));
    }

    @Test
    @DisplayName("팀 생성 테스트")
    void create() throws Exception {
        MemberResponseDto memberA = memberManager.createMember("memberA", "11");
        MemberResponseDto memberB = memberManager.createMember("memberB", "11");
        MemberResponseDto memberC = memberManager.createMember("memberC", "11");

        List<Long> members = new ArrayList<>();
        members.add(memberB.getId());
        members.add(memberC.getId());

        Map<Long, Integer> map = new HashMap<>();
        map.put(memberB.getId(), 1);
        map.put(memberC.getId(), 2);

        String res = mockMvc.perform(post("/teams/study/")
                        .content(objectMapper.writeValueAsString(StudyTeamCreateDto.builder()
                                .groupNumbers(map)
                                .leaderId(memberA.getId())
                                .members(members)
                                .groupName("team1")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        StudyTeamResponseDto response = objectMapper.readValue(res, StudyTeamResponseDto.class);

        List<AttendanceManageDto> attendances = new ArrayList<>();
        attendances.add(AttendanceManageDto.builder()
                .week(1)
                .isAttend(true)
                .memberId(memberB.getId())
                .build());
        attendances.add(AttendanceManageDto.builder()
                .week(1)
                .isAttend(true)
                .memberId(memberC.getId())
                .build());

        mockMvc.perform(post("/teams/study/details/")
                        .content(objectMapper.writeValueAsString(StudyDetailCreateDto.builder()
                                .week(1)
                                .teamId(response.getId())
                                .content("1주차 활동")
                                .attendances(attendances)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(post("/teams/study/details/")
                        .content(objectMapper.writeValueAsString(StudyDetailCreateDto.builder()
                                .week(1)
                                .teamId(response.getId())
                                .content("1주차 활동")
                                .attendances(attendances)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(get("/teams/study/details/2"))
                .andDo(MockMvcResultHandlers.print());
    }

}
