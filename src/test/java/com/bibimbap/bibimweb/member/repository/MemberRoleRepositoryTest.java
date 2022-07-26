package com.bibimbap.bibimweb.member.repository;

import com.bibimbap.bibimweb.member.domain.Member;
import com.bibimbap.bibimweb.member.domain.MemberRole;
import com.bibimbap.bibimweb.member.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRoleRepositoryTest {

    @Autowired
    MemberRoleRepository memberRoleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("해당 유저의 모든 역할군 검색")
    void findAllByMemberId() {
        Member member1 = memberRepository.save(Member.builder().name("member1").build());
        Member member2 = memberRepository.save(Member.builder().name("member2").build());

        Role role1 = roleRepository.save(Role.builder().groupName("비빔밥").role("운영진").build());
        Role role2 = roleRepository.save(Role.builder().groupName("자바").role("튜터").build());
        Role role3 = roleRepository.save(Role.builder().groupName("스프링").role("멤버").build());

        MemberRole mr1 = memberRoleRepository.save(MemberRole.builder().member(member1).role(role1).build());
        MemberRole mr2 =memberRoleRepository.save(MemberRole.builder().member(member1).role(role2).build());
        MemberRole mr3 =memberRoleRepository.save(MemberRole.builder().member(member1).role(role3).build());

        memberRoleRepository.save(MemberRole.builder().member(member2).role(role1).build());
        memberRoleRepository.save(MemberRole.builder().member(member2).role(role2).build());

        List<MemberRole> list = memberRoleRepository.findAllByMemberId(member1.getId());
        assertThat(list.size()).isEqualTo(3);
        assertThat(list).contains(mr1,mr2,mr3);
    }

    @Test
    @DisplayName("해당 역할군의 모든 유저 검색")
    void findAllByRoleId() {
        Member member1 = memberRepository.save(Member.builder().name("member1").build());
        Member member2 = memberRepository.save(Member.builder().name("member2").build());
        Member member3 = memberRepository.save(Member.builder().name("member3").build());

        Role role1 = roleRepository.save(Role.builder().groupName("비빔밥").role("운영진").build());
        Role role2 = roleRepository.save(Role.builder().groupName("자바").role("튜터").build());

        MemberRole mr1 = memberRoleRepository.save(MemberRole.builder().member(member1).role(role1).build());
        MemberRole mr2 =memberRoleRepository.save(MemberRole.builder().member(member2).role(role1).build());
        MemberRole mr3 =memberRoleRepository.save(MemberRole.builder().member(member3).role(role1).build());

        memberRoleRepository.save(MemberRole.builder().member(member1).role(role2).build());
        memberRoleRepository.save(MemberRole.builder().member(member2).role(role2).build());
        memberRoleRepository.save(MemberRole.builder().member(member3).role(role2).build());

        List<MemberRole> list = memberRoleRepository.findAllByRoleId(role1.getId());
        assertThat(list.size()).isEqualTo(3);
        assertThat(list).contains(mr1, mr2, mr3);
    }

    @Test
    @DisplayName("특정 멤버와 역할군으로 검색")
    void findByMemberIdAndRoleId() {

        Member member1 = memberRepository.save(Member.builder().name("member1").build());
        Member member2 = memberRepository.save(Member.builder().name("member2").build());
        Member member3 = memberRepository.save(Member.builder().name("member3").build());

        Role role1 = roleRepository.save(Role.builder().groupName("비빔밥").role("운영진").build());
        Role role2 = roleRepository.save(Role.builder().groupName("자바").role("튜터").build());

        MemberRole mr1 = memberRoleRepository.save(MemberRole.builder().member(member1).role(role1).build());
        MemberRole mr2 =memberRoleRepository.save(MemberRole.builder().member(member2).role(role1).build());
        MemberRole mr3 =memberRoleRepository.save(MemberRole.builder().member(member3).role(role1).build());

        MemberRole saved = memberRoleRepository.save(MemberRole.builder().member(member1).role(role2).build());
        memberRoleRepository.save(MemberRole.builder().member(member2).role(role2).build());
        memberRoleRepository.save(MemberRole.builder().member(member3).role(role2).build());

        MemberRole memberRole = memberRoleRepository.findByMemberIdAndRoleId(member1.getId(), role1.getId()).get();
        assertThat(memberRole).isSameAs(mr1);
    }
}