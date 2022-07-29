package com.bibimbap.bibimweb.test.service;

import com.bibimbap.bibimweb.test.domain.TMember;
import com.bibimbap.bibimweb.test.repository.TMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TMemberService {

    private final TMemberRepository memberRepository;

    public TMember createMember(TMember member) {
        return memberRepository.save(member);
    }
}
