package com.bibimbap.bibimweb.service.role;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.role.member.AdminRole;
import com.bibimbap.bibimweb.domain.role.member.HonorRole;
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

    public void addHonorRole(Long memberId, String groupName) {
        Member member = memberRepository.findById(memberId).get();
        HonorRole saved = honorRoleRepository.save(HonorRole.builder()
                .rollName("HONOR")
                .groupName(groupName)
                .member(member)
                .team(null)
                .build());
        member.getRoles().add(saved);
    }

    public void updateAdminRole(Long memberId, String position) {
        Member member = memberRepository.findById(memberId).get();
        AdminRole adminRole = member.getRoles().stream()
                .filter(role -> role instanceof AdminRole)
                .map(role -> (AdminRole) role)
                .findAny().get();
        adminRole.setPosition(position);
        adminRoleRepository.save(adminRole);
    }

    public void updateHonorRole(Long memberId, String groupName) {
        Member member = memberRepository.findById(memberId).get();
        HonorRole honorRole = member.getRoles().stream()
                .filter(role -> role instanceof HonorRole)
                .map(role -> (HonorRole) role)
                .findAny().get();
        honorRole.setGroupName(groupName);
        honorRoleRepository.save(honorRole);
    }

    public void deleteAdminRole(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        Role adminRole = member.getRoles().stream()
                .filter(role -> role instanceof AdminRole)
                .findAny().get();
        member.getRoles().remove(adminRole);
        adminRoleRepository.deleteById(adminRole.getId());
    }

    public void deleteHonorRole(Long memberId, String groupName) {
        Member member = memberRepository.findById(memberId).get();
        Role target = member.getRoles().stream()
                .filter(role -> role instanceof HonorRole
                        && ((HonorRole) role).getGroupName().equals(groupName))
                .findAny().get();
        member.getRoles().remove(target);
        honorRoleRepository.deleteById(target.getId());
    }

}
