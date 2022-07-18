package com.bibimbap.bibimweb.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bibimbap.bibimweb.member.domain.Member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Member addMember() throws Exception {
        Member member = new Member();
        member.setAttendance("000");
        member.setName("test");
        String content = objectMapper.writeValueAsString(member);
        MvcResult mvcResult = mockMvc.perform(post("/member/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Member.class);
    }

    @Test
    @DisplayName("멤버 리스트 테스트")
    void getMemberList() throws Exception {

        addMember();
        addMember();

        mockMvc.perform(get("/member/")
                        .queryParam("pageNumber", "0")
                        .queryParam("pageSize", "5"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("멤버 조회 테스트")
    void getMemberById() throws Exception {
        Member member = addMember();
        Long id = member.getId();
        mockMvc.perform(get("/member/" + id))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("멤버 생성 테스트")
    void createMember() throws Exception {
        Member member = new Member();
        member.setAttendance("000");
        member.setName("test");
        String content = objectMapper.writeValueAsString(member);
        mockMvc.perform(post("/member/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("멤버 수정 테스트")
    void updateMember() throws Exception {
        Member member = addMember();
        member.setName("UpdateMember");
        String content = objectMapper.writeValueAsString(member);
        System.out.println(member);
        mockMvc.perform(put("/member/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("멤버 삭제 테스트")
    void deleteMemberById() throws Exception {
        Member member = addMember();
        Long id = member.getId();
        mockMvc.perform(delete("/member/" + id))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}