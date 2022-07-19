package com.bibimbap.bibimweb.member.controller;

import com.bibimbap.bibimweb.member.domain.Member;
import com.bibimbap.bibimweb.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public List<Member> getMemberList(Pageable pageable) {
        return memberRepository.findAll(pageable).getContent();
    }

    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable Long id) {
        return memberRepository.findById(id).get();
    }

    @PostMapping("/")
    public Member createMember(@RequestBody Member member) {
        return memberRepository.save(member);
    }

    @PutMapping("/")
    public Member updateMember(@RequestBody Member member) {
        return memberRepository.save(member);
    }

    @DeleteMapping("/{id}")
    public void deleteMemberById(@PathVariable Long id) {
        memberRepository.deleteById(id);
    }
}
