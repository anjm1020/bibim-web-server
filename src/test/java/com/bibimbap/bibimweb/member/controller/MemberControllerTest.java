package com.bibimbap.bibimweb.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bibimbap.bibimweb.member.domain.Member;

import com.bibimbap.bibimweb.member.domain.MemberRole;
import com.bibimbap.bibimweb.member.domain.Role;
import com.bibimbap.bibimweb.member.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.member.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.member.dto.member.MemberUpdateDto;
import com.bibimbap.bibimweb.member.dto.role.RoleResponseDto;
import com.bibimbap.bibimweb.member.dto.role.RoleUpdateDto;
import com.bibimbap.bibimweb.member.repository.MemberRepository;
import com.bibimbap.bibimweb.member.repository.MemberRoleRepository;
import com.bibimbap.bibimweb.member.repository.RoleRepository;
import com.bibimbap.bibimweb.util.exception.ConflictException;
import com.bibimbap.bibimweb.util.exception.NotFoundException;
import com.bibimbap.bibimweb.util.exception.OutOfRangeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberRoleRepository memberRoleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ModelMapper mapper = new ModelMapper();

    Member addMember() throws Exception {
        Member member = new Member();
        member.setAttendance("000");
        member.setName("test");
        String content = objectMapper.writeValueAsString(member);
        MvcResult mvcResult = mockMvc.perform(post("/members/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Member.class);
    }

    Member addMemberManually(String name, String id) {
        return memberRepository.save(Member.builder().name(name).studentId(id).build());
    }

    Role addRoleManually(String role, String group) throws Exception {
        return roleRepository.save(Role.builder().role(role).groupName(group).build());
    }

    @Test
    @DisplayName("멤버 리스트 테스트")
    void getMemberList() throws Exception {

        addMember();
        addMember();

        mockMvc.perform(get("/members/")
                        .queryParam("page", "0")
                        .queryParam("size", "5"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // page exception
        mockMvc.perform(get("/members/")
                        .queryParam("page", "20")
                        .queryParam("size", "5"))
                .andExpect(
                        result -> Assertions.assertTrue(result.getResolvedException().getClass()
                                .isAssignableFrom(OutOfRangeException.class))
                )
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("해당 역할 멤버 리스트 조회 테스트")
    void getMemberByRole() throws Exception {
        Role role1 = addRoleManually("운영진", "비빔밥");
        Role role2 = addRoleManually("팀장", "자바");

        Member member1 = addMemberManually("member1", "1000");
        Member member2 = addMemberManually("member2", "1000");
        Member member3 = addMemberManually("member3", "1000");

        MemberRole mr1 = memberRoleRepository.save(MemberRole.builder().role(role1).member(member1).build());
        MemberRole mr2 = memberRoleRepository.save(MemberRole.builder().role(role1).member(member2).build());
        MemberRole mr3 = memberRoleRepository.save(MemberRole.builder().role(role2).member(member2).build());

        mockMvc.perform(get("/members/?roleId=" + role1.getId()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("단일 멤버 조회 테스트")
    void getMemberById() throws Exception {
        Member member = addMember();
        Long id = member.getId();
        mockMvc.perform(get("/members/" + id))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("멤버 생성 테스트")
    void createMember() throws Exception {

        Member member = new Member();
        member.setName("test");
        member.setStudentId("2000");
        String content = objectMapper.writeValueAsString(member);
        mockMvc.perform(post("/members/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(post("/members/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        (result) -> Assertions.assertTrue(result.getResolvedException().getClass()
                                .isAssignableFrom(ConflictException.class))
                )
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("멤버 수정 테스트")
    void updateMember() throws Exception {
        Member member = addMember();
        member.setName("UpdateMember");
        RoleUpdateDto roleDto = RoleUpdateDto.builder()
                .groupName("testGroup")
                .role("leader")
                .status(1)
                .build();

        List<RoleUpdateDto> roles = new ArrayList<>();
        roles.add(roleDto);

        MemberUpdateDto dto = MemberUpdateDto.builder()
                .id(member.getId())
                .name(member.getName())
                .attendance("1")
                .studentId("201911190")
                .roles(roles)
                .build();

        String content = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/members/")
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
        mockMvc.perform(delete("/members/" + id))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("시나리오 테스트 ")
    void scenarioTest() throws Exception {
        // 1. member create
        System.out.println("=================================================================");
        System.out.println("        MEMBER CREATE");
        final int MEMBER_SIZE = 10;
        List<MemberCreateDto> createDtoList = new ArrayList<>();
        List<MemberResponseDto> memberList = new ArrayList<>();

        for (int i = 1; i <= MEMBER_SIZE; i++) {
            createDtoList.add(MemberCreateDto.builder()
                    .name("member" + i)
                    .studentId(1000 * i + "")
                    .build());
        }

        for (MemberCreateDto dto : createDtoList) {
            String content = mockMvc.perform(post("/members/")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            memberList.add(objectMapper.readValue(content, MemberResponseDto.class));
        }

        System.out.println("=================================================================");
        System.out.println("        Conflict Exception : 중복 멤버");
        // Conflict Exception
        mockMvc.perform(post("/members/")
                        .content(objectMapper.writeValueAsString(createDtoList.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getClass()
                        .isAssignableFrom(ConflictException.class)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage()
                        .equals(ConflictException.MEMBER.getMessage())))
                .andDo(MockMvcResultHandlers.print());


        // 2. member update
        System.out.println("=================================================================");
        System.out.println("        Member Update");
        MemberResponseDto targetMember = memberList.get(1);
        mockMvc.perform(put("/members/")
                        .content(objectMapper.writeValueAsString(
                                MemberUpdateDto.builder()
                                        .id(targetMember.getId())
                                        .name("updateMember")
                                        .attendance("1111")
                                        .studentId(targetMember.getStudentId())
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        System.out.println("=================================================================");
        System.out.println("        404 Exception");
        mockMvc.perform(put("/members/")
                        .content(objectMapper.writeValueAsString(
                                MemberUpdateDto.builder()
                                        .id(9999L)
                                        .name("updateMember")
                                        .attendance("1111")
                                        .studentId(targetMember.getStudentId())
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getClass()
                        .isAssignableFrom(NotFoundException.class)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage()
                        .equals(NotFoundException.MEMBER.getMessage())))
                .andDo(MockMvcResultHandlers.print());

        // 3. memberRole create
        System.out.println("=================================================================");
        System.out.println("        MemberRole Create");
        List<RoleUpdateDto> roles = new ArrayList<>();
        roles.add(RoleUpdateDto.builder()
                .role("운영진")
                .groupName("비빔밥")
                .status(1)
                .build());
        String content = "";
        for (int i = 2; i < 6; i++) {
            MemberResponseDto currMember = memberList.get(i);
            content = mockMvc.perform(put("/members/")
                            .content(objectMapper.writeValueAsString(
                                    MemberUpdateDto.builder()
                                            .id(currMember.getId())
                                            .name(currMember.getName())
                                            .studentId(currMember.getStudentId())
                                            .attendance(currMember.getAttendance())
                                            .roles(roles)
                                            .build()
                            ))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn()
                    .getResponse().getContentAsString();
        }
        // 4. memberRole update
        System.out.println("=================================================================");
        System.out.println("        MemberRole Update");

        MemberResponseDto roleUpdateTarget = objectMapper.readValue(content, MemberResponseDto.class);

        RoleResponseDto dto = roleUpdateTarget.getRoles().get(0);
        List<RoleUpdateDto> newRoles = new ArrayList<>();
        newRoles.add(RoleUpdateDto.builder()
                .id(dto.getId())
                .groupName(dto.getGroupName())
                .status(1)
                .role("일반")
                .build());


        mockMvc.perform(put("/members/")
                        .content(objectMapper.writeValueAsString(
                                MemberUpdateDto.builder()
                                        .id(roleUpdateTarget.getId())
                                        .name(roleUpdateTarget.getName())
                                        .studentId(roleUpdateTarget.getStudentId())
                                        .attendance(roleUpdateTarget.getAttendance())
                                        .roles(newRoles)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // 5. memberRole delete
        System.out.println("=================================================================");
        System.out.println("        MemberRole delete");
        List<RoleUpdateDto> deleteRoles = new ArrayList<>();
        deleteRoles.add(RoleUpdateDto.builder()
                .id(dto.getId())
                .groupName(dto.getGroupName())
                .status(2)
                .role(dto.getRole())
                .build());

        mockMvc.perform(put("/members/")
                        .content(objectMapper.writeValueAsString(
                                MemberUpdateDto.builder()
                                        .id(roleUpdateTarget.getId())
                                        .name(roleUpdateTarget.getName())
                                        .studentId(roleUpdateTarget.getStudentId())
                                        .attendance(roleUpdateTarget.getAttendance())
                                        .roles(deleteRoles)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // 6. member list
        System.out.println("=================================================================");
        System.out.println("        Member List");
        String res = mockMvc.perform(get("/members/")
                        .queryParam("size", "10")
                        .queryParam("page", "0"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MemberResponseDto[] memResList = objectMapper.readValue(res, MemberResponseDto[].class);
        for (MemberResponseDto memberResponseDto : memResList) {
            System.out.println(memberResponseDto);
        }

        System.out.println("=================================================================");
        System.out.println("        Member List : Page Exception");
        mockMvc.perform(get("/members/")
                        .queryParam("size", "10")
                        .queryParam("page", "99"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getClass()
                        .isAssignableFrom(OutOfRangeException.class)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage()
                        .equals(OutOfRangeException.PAGE.getMessage())))
                .andDo(MockMvcResultHandlers.print());

        // 7. member list by role
        System.out.println("=================================================================");
        System.out.println("        Member List By Role");
        String byRoleRes = mockMvc.perform(get("/members/")
                        .queryParam("groupName", "비빔밥")
                        .queryParam("role", "운영진"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MemberResponseDto[] byRoleMembers = objectMapper.readValue(byRoleRes, MemberResponseDto[].class);
        for (MemberResponseDto byRoleMember : byRoleMembers) {
            System.out.println(byRoleMember);
        }

        // 8. member delete
        System.out.println("=================================================================");
        System.out.println("        Member Delete");
        List<MemberRole> allBefore = memberRoleRepository.findAll();
        for (MemberRole memberRole : allBefore) {
            System.out.println(memberRole);
        }
        mockMvc.perform(delete("/members/3"))
//                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        List<MemberRole> all = memberRoleRepository.findAll();
        for (MemberRole memberRole : all) {
            System.out.println(memberRole);
        }

    }
}