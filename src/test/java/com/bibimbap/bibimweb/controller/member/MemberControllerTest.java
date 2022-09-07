package com.bibimbap.bibimweb.controller.member;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bibimbap.bibimweb.dto.member.role.AdminMemberDto;
import com.bibimbap.bibimweb.dto.member.role.AdminMemberResponseDto;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.bibimbap.bibimweb.service.role.MemberRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRoleService memberRoleService;

    @Autowired
    MemberManager memberManager;

    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @DisplayName("운영자 리스트 테스트")
    void adminList() throws Exception {
        MemberResponseDto member = memberManager.createMember("name1", "111");
        MemberResponseDto member2 = memberManager.createMember("name2", "222");
        MemberResponseDto member3 = memberManager.createMember("name3", "333");

        memberRoleService.addAdminRole(AdminMemberDto.builder()
                .memberId(member.getId())
                .position("회장")
                .build());
        memberRoleService.addAdminRole(AdminMemberDto.builder()
                .memberId(member2.getId())
                .position("부회장")
                .build());

        String content = mockMvc.perform(get("/members/admin/"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
        AdminMemberResponseDto[] list = objectMapper.readValue(content, AdminMemberResponseDto[].class);
        assertThat(Arrays.stream(list)
                .anyMatch(dto -> dto.getId().equals(member.getId())
                        && dto.getPosition().equals("회장")));
        assertThat(Arrays.stream(list)
                .anyMatch(dto -> dto.getId().equals(member2.getId())
                        && dto.getPosition().equals("부회장")));
    }

}
