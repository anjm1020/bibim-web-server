package com.bibimbap.bibimweb.service.member;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.role.member.AdminRole;
import com.bibimbap.bibimweb.dto.member.AdminMemberResponseDto;
import com.bibimbap.bibimweb.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.AdminRoleRepository;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.bibimbap.bibimweb.service.role.MemberRoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRoleService memberRoleService;
    @Autowired
    MemberManager memberManager;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AdminRoleRepository adminRoleRepository;


    @Test
    @DisplayName("일반회원 생성 테스트")
    void createMember() {
        String name = "memberA";
        String studentId = "1111";
        MemberResponseDto saved = memberService.createMember(MemberCreateDto.builder()
                .name(name)
                .studentId(studentId)
                .build());

        Member memberA = memberRepository.findById(saved.getId()).get();
        assertThat(memberA.getName()).isEqualTo(name);
        assertThat(memberA.getStudentId()).isEqualTo(studentId);
        assertThat(memberA.getRoles().size()).isEqualTo(0);

    }

    @Test
    @DisplayName("운영진 생성 테스트")
    void createAdmin() {
        String name = "memberA";
        String studentId = "111";
        MemberResponseDto saved = memberManager.createMember(name, studentId);

        String position = "부회장";
        memberRoleService.addAdminRole(saved.getId(), position);
        Member member = memberRepository.findById(saved.getId()).get();
        Optional<Role> adminRole = member.getRoles().stream().filter(role -> role.getRollName().equals("ADMIN")).findAny();
        assertThat(adminRole.isPresent()).isTrue();
        assertThat(adminRole.get() instanceof AdminRole).isTrue();
        assertThat(((AdminRole) adminRole.get()).getPosition().equals(position)).isTrue();

        List<AdminRole> all = adminRoleRepository.findAll();
        assertThat(all.stream().anyMatch(role -> role.getMember().getId().equals(member.getId())
                && role.getRollName().equals("ADMIN")
                && role.getPosition().equals(position))).isTrue();
    }

    @Test
    @DisplayName("운영진 삭제 테스트")
    void deleteAdmin() {

    }

    @Test
    @DisplayName("명예회원 생성 테스트")
    void createHonor() {

    }

    @Test
    @DisplayName("명예회원 삭제 테스트")
    void deleteHonor() {

    }

    @Test
    @DisplayName("회원 일반 필드 수정 테스트")
    void updateNormField() {

    }

    @Test
    @DisplayName("회원 리스트 - 페이지네이션 테스트")
    void getListByPage() {

    }

    @Test
    @DisplayName("운영진 리스트 - 년도별, 전체 테스트")
    void getAdminList() {
        String nameA = "memberA";
        String nameB = "memberB";
        MemberResponseDto memberA = memberManager.createMember(nameA, "1");
        MemberResponseDto memberB = memberManager.createMember(nameB, "2");

        memberRoleService.addAdminRole(memberA.getId(), "회장");
        memberRoleService.addAdminRole(memberB.getId(), "부회장");

        for (AdminRole adminRole : adminRoleRepository.findAll()) {
            System.out.println(adminRole.getMember().getName() + "/" + adminRole.getPosition() + "/" + adminRole.getPeriod());
        }
        List<AdminRole> adminRoles = adminRoleRepository.findAll();
        assertThat(adminRoles.size()).isEqualTo(2);
        assertThat(adminRoles.stream()
                .anyMatch(role -> role.getPosition().equals("회장")
                        || role.getPosition().equals("부회장"))).isTrue();

        List<AdminMemberResponseDto> list = memberService.getAdminMemberList();
        for (AdminMemberResponseDto adminMemberResponseDto : list) {
            System.out.println(adminMemberResponseDto);
        }
        assertThat(list.size()).isEqualTo(2);


    }

    @Test
    @DisplayName("명예회원 리스트 - 년도별, 전체 테스트")
    void getHonorList() {

    }

    @Test
    @DisplayName("회원 삭제 시 Role 제대로 업데이트 되는지 테스트")
    void checkRoleTableAfterDelete() {

    }

}