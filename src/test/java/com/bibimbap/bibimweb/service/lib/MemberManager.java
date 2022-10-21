package com.bibimbap.bibimweb.service.lib;

import com.bibimbap.bibimweb.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MemberManager {

    @Autowired
    MemberService memberService;

    public MemberResponseDto createMember(String name, String studentId) {
        return memberService.createMember(MemberCreateDto.builder()
                .name(name)
                .studentId(studentId)
                .build());
    }
}
