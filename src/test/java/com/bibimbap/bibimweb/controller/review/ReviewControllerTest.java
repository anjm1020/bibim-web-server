package com.bibimbap.bibimweb.controller.review;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bibimbap.bibimweb.domain.team.StudyTeam;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.review.ReviewCreateDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamResponseDto;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.bibimbap.bibimweb.service.team.StudyTeamService;
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
import java.util.Map;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    StudyTeamService studyTeamService;
    @Autowired
    MemberManager memberManager;
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();


    @Test
    @DisplayName("생성 -> Json 체크")
    void check() throws Exception {
        MemberResponseDto memberA = memberManager.createMember("memberA", "1");
        ArrayList<Long> members = new ArrayList<>();
        members.add(memberA.getId());
        Map<Long, Integer> groupMap = new HashMap<>();
        groupMap.put(memberA.getId(), 1);
        StudyTeamResponseDto team1 = studyTeamService.createStudyTeam(StudyTeamCreateDto.builder()
                .groupName("team1")
                .leaderId(memberA.getId())
                .members(members)
                .groupNumbers(groupMap)
                .build());
        mockMvc.perform(post("/reviews/")
                        .content(mapper.writeValueAsString(ReviewCreateDto.builder()
                                .content("content")
                                .teamId(team1.getId())
                                .memberId(memberA.getId())
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
