package com.bibimbap.bibimweb.service.role;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.member.AdminRole;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.AdminRoleRepository;
import com.bibimbap.bibimweb.repository.role.HonorRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRoleService {

    private final MemberRepository memberRepository;
    private final AdminRoleRepository adminRoleRepository;
    private final HonorRoleRepository honorRoleRepository;

    public void addAdminRole(Long memberId, String position) {
        Member member = memberRepository.findById(memberId).get();
        AdminRole saved = adminRoleRepository.save(AdminRole.builder()
                .rollName("ADMIN")
                .position(position)
                .member(member)
                .team(null)
                .build());
        member.getRoles().add(saved);
    }

    public void updateAdminRole(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        member.getRoles().stream()
                .filter(role -> role instanceof AdminRole)
                .findAny();
    }

}
