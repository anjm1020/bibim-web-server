package com.bibimbap.bibimweb.service.role;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.role.member.AdminRole;
import com.bibimbap.bibimweb.domain.role.member.HonorRole;
import com.bibimbap.bibimweb.dto.member.role.AdminMemberDto;
import com.bibimbap.bibimweb.dto.member.role.AdminMemberResponseDto;
import com.bibimbap.bibimweb.dto.member.role.HonorMemberDto;
import com.bibimbap.bibimweb.dto.member.role.HonorMemberResponseDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.AdminRoleRepository;
import com.bibimbap.bibimweb.repository.role.HonorRoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRoleService {

    private final MemberRepository memberRepository;
    private final AdminRoleRepository adminRoleRepository;
    private final HonorRoleRepository honorRoleRepository;

    private final ModelMapper mapper = new ModelMapper();
    public AdminMemberResponseDto addAdminRole(AdminMemberDto dto) {
        Long memberId = dto.getMemberId();
        String position = dto.getPosition();
        Member member = memberRepository.findById(memberId).get();
        AdminRole saved = adminRoleRepository.save(AdminRole.builder()
                .rollName("ADMIN")
                .position(position)
                .member(member)
                .team(null)
                .build());
        member.getRoles().add(saved);
        AdminMemberResponseDto res = mapper.map(member, AdminMemberResponseDto.class);
        res.setPosition(saved.getPosition());
        res.setPeriod(saved.getPeriod());
        return res;
    }

    public HonorMemberResponseDto addHonorRole(HonorMemberDto dto) {
        Long memberId = dto.getMemberId();
        String groupName = dto.getGroupName();
        Member member = memberRepository.findById(memberId).get();
        HonorRole saved = honorRoleRepository.save(HonorRole.builder()
                .rollName("HONOR")
                .groupName(groupName)
                .member(member)
                .team(null)
                .build());
        member.getRoles().add(saved);
        HonorMemberResponseDto res = mapper.map(member, HonorMemberResponseDto.class);
        res.setGroupName(saved.getGroupName());
        res.setPeriod(saved.getPeriod());
        return res;
    }

    public AdminMemberResponseDto updateAdminRole(AdminMemberDto dto) {
        Long memberId = dto.getMemberId();
        String position = dto.getPosition();
        Member member = memberRepository.findById(memberId).get();
        AdminRole adminRole = member.getRoles().stream()
                .filter(role -> role instanceof AdminRole)
                .map(role -> (AdminRole) role)
                .findAny().get();
        adminRole.setPosition(position);
        adminRoleRepository.save(adminRole);
        AdminMemberResponseDto res = mapper.map(member, AdminMemberResponseDto.class);
        res.setPosition(adminRole.getPosition());
        res.setPeriod(adminRole.getPeriod());
        return res;
    }

    public HonorMemberResponseDto updateHonorRole(HonorMemberDto dto) {
        Long memberId = dto.getMemberId();
        String groupName = dto.getGroupName();
        Member member = memberRepository.findById(memberId).get();
        HonorRole honorRole = member.getRoles().stream()
                .filter(role -> role instanceof HonorRole)
                .map(role -> (HonorRole) role)
                .findAny().get();
        honorRole.setGroupName(groupName);
        honorRoleRepository.save(honorRole);
        HonorMemberResponseDto res = mapper.map(member, HonorMemberResponseDto.class);
        res.setGroupName(honorRole.getGroupName());
        res.setPeriod(honorRole.getPeriod());
        return res;
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
