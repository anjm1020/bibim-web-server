package com.bibimbap.bibimweb.test;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.role.member.AdminRole;
import com.bibimbap.bibimweb.dto.member.role.AdminMemberDto;
import com.bibimbap.bibimweb.dto.member.role.AdminMemberResponseDto;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.AdminRoleRepository;
import com.bibimbap.bibimweb.repository.role.HonorRoleRepository;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.bibimbap.bibimweb.service.member.MemberService;
import com.bibimbap.bibimweb.service.role.MemberRoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class TemporaryTest {

    @Autowired
    MemberManager memberManager;
    @Autowired
    MemberRoleService memberRoleService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AdminRoleRepository adminRoleRepository;
    @Autowired
    HonorRoleRepository honorRoleRepository;

    @Test
    @DisplayName("운영진 생성 -> 조회 -> 수정 -> 삭제")
    void integratedTestAdmin() {
        String nameA = "memberA";
        String idA = "111";
        MemberResponseDto memberADto = memberManager.createMember(nameA, idA);

        // WHEN : 운영진 role 추가
        String positionA = "운영진";
        memberRoleService.addAdminRole(AdminMemberDto.builder()
                .memberId(memberADto.getId())
                .position(positionA)
                .build());

        // THEN
        Member findMemberA = memberRepository.findById(memberADto.getId()).get();
        List<Role> aRoles = findMemberA.getRoles();
        assertThat(aRoles.stream()
                .anyMatch(role -> role instanceof AdminRole
                        && ((AdminRole) role).getPosition().equals(positionA))).isTrue();

        String nameB = "memberB";
        String idB = "222";
        MemberResponseDto memberBDto = memberManager.createMember(nameB, idB);

        String positionB = "총무";
        memberRoleService.addAdminRole(AdminMemberDto.builder()
                .memberId(memberBDto.getId())
                .position(positionB)
                .build());

        String positionAUpdated = "부회장";
        // WHEN : 운영진A 수정
        memberRoleService.updateAdminRole(AdminMemberDto.builder()
                .memberId(memberADto.getId())
                .position(positionAUpdated)
                .build());
        // THEN
        Member findUpdatedA = memberRepository.findById(memberADto.getId()).get();
        List<Role> updatedRoles = findUpdatedA.getRoles();
        assertThat(updatedRoles.stream()
                .anyMatch(role -> role instanceof AdminRole
                        && ((AdminRole) role).getPosition().equals(positionAUpdated))).isTrue();

        List<AdminMemberResponseDto> adminMemberListBeforeDelete = memberService.getAdminMemberList();
        assertThat(adminMemberListBeforeDelete.size()).isEqualTo(2);
        assertThat(adminMemberListBeforeDelete.stream()
                .anyMatch(member -> member.getId().equals(memberADto.getId())
                        && member.getPosition().equals(positionAUpdated)));
        assertThat(adminMemberListBeforeDelete.stream()
                .anyMatch(member -> member.getId().equals(memberBDto.getId())
                        && member.getPosition().equals(positionB)));

        // WHEN
        memberRoleService.deleteAdminRole(memberADto.getId());

        Member memberA = memberRepository.findById(memberADto.getId()).get();
        assertThat(memberA.getRoles().size()).isEqualTo(0);
        List<AdminMemberResponseDto> adminMemberListAfterDelete = memberService.getAdminMemberList();
        assertThat(adminMemberListAfterDelete.size()).isEqualTo(1);
        assertThat(adminMemberListBeforeDelete.stream()
                .anyMatch(member -> member.getId().equals(memberBDto.getId())
                        && member.getPosition().equals(positionB)));
    }

    @Test
    @DisplayName("명예회원 생성 -> 조회 -> 수정 -> 삭제")
    void integratedTestHonor() {

    }
}
