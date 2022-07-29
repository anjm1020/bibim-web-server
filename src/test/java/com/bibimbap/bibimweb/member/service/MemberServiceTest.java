package com.bibimbap.bibimweb.member.service;

import com.bibimbap.bibimweb.member.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.member.dto.member.MemberResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    ModelMapper mapper = new ModelMapper();

    MemberResponseDto saveMember(String name, String sid) {
        MemberCreateDto dto = MemberCreateDto.builder()
                .name(name)
                .studentId(sid)
                .build();

        return memberService.createMember(dto);
    }

    @Test
    @DisplayName("멤버 생성 테스트")
    void createMember() {

        MemberResponseDto response1 = saveMember("jaemin", "1111");

        assertThat(response1.getName()).isEqualTo("jaemin");
        assertThat(response1.getStudentId()).isEqualTo("1111");
        System.out.println(response1);
    }

    @Test
    @DisplayName("멤버 리스트 테스트")
    void readMemberList() {

        MemberResponseDto memberA = saveMember("memberA", "11");
        MemberResponseDto memberB = saveMember("memberB", "22");

        PageRequest page = PageRequest.of(0, 10);
        List<MemberResponseDto> result = memberService.getMemberList(page);

        assertThat(result.size()).isEqualTo(2);

        for (MemberResponseDto memberResponseDto : result) {
            System.out.println(memberResponseDto);
        }
    }

    @Test
    @DisplayName("멤버 리스트 by Role 테스트")
    void readMemberListByRole() {

    }

    @Test
    @DisplayName("멤버 단일 조회 테스트")
    void readMemberById() {

        MemberResponseDto memberA = saveMember("memberA", "11");
        MemberResponseDto memberB = saveMember("memberB", "11");

        MemberResponseDto res = memberService.getMemberById(memberA.getId());
        assertThat(res.getId()).isEqualTo(memberA.getId());
        System.out.println(res);
    }
}
