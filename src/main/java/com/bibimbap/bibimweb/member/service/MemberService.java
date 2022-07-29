package com.bibimbap.bibimweb.member.service;

import com.bibimbap.bibimweb.member.domain.Member;
import com.bibimbap.bibimweb.member.domain.MemberRole;
import com.bibimbap.bibimweb.member.domain.Role;
import com.bibimbap.bibimweb.member.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.member.dto.member.MemberUpdateDto;
import com.bibimbap.bibimweb.member.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.member.dto.role.RoleDto;
import com.bibimbap.bibimweb.member.dto.role.RoleResponseDto;
import com.bibimbap.bibimweb.member.dto.role.RoleUpdateDto;
import com.bibimbap.bibimweb.member.repository.MemberRepository;
import com.bibimbap.bibimweb.member.repository.MemberRoleRepository;
import com.bibimbap.bibimweb.member.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final RoleService roleService;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;

    private final int STATUS_NONE = 0;
    private final int STATUS_ADD = 1;
    private final int STATUS_REMOVE = 2;

    private ModelMapper mapper = new ModelMapper();

    public boolean isExistStudent(String id) {
        return memberRepository.existsByStudentId(id);
    }

    public boolean isExistMember(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    public boolean isValidPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long count = memberRepository.count();
        return 0 <= pageNumber && pageNumber <= ((count - 1) / pageSize);
    }

    // 멤버 생성
    public MemberResponseDto createMember(MemberCreateDto dto) {
        Member newMember = mapper.map(dto, Member.class);
        return addRolesToDto(mapper.map(memberRepository.save(newMember), MemberResponseDto.class));
    }

    // 멤버 리스트 조회
    public List<MemberResponseDto> getMemberList(Pageable pageable) {
        return mapMemberList(memberRepository.findAll(pageable).getContent())
                .stream()
                .map(m->addRolesToDto(m))
                .collect(Collectors.toList());
    }

    // 특정 role의 멤버 리스트 조회
    public List<MemberResponseDto> getMemberListByRole(String groupName, String role) {
        List<Role> roleList = roleRepository.findAllByGroupNameAndRole(groupName, role);
        List<MemberResponseDto> res = new ArrayList<>();
        for (Role curr : roleList) {
            Long id = curr.getId();
            memberRoleRepository.findAllByRoleId(id).stream()
                    .forEach(mr -> res.add(addRolesToDto(mapper.map(mr.getMember(),MemberResponseDto.class))));
        }
        return res;
    }

    // id 로 단일 조회
    public MemberResponseDto getMemberById(Long id) {
        return addRolesToDto(mapper.map(memberRepository.findById(id).get(), MemberResponseDto.class));
    }

    // 멤버 필드 및 role 수정
    public MemberResponseDto updateMember(MemberUpdateDto dto) {

        Member member = memberRepository.findById(dto.getId()).get();
        List<RoleUpdateDto> roles = dto.getRoles();
        for (RoleUpdateDto role : roles) {
            updateRoleOfMember(member, role);
        }
        Member savedMember = memberRepository.save(mapper.map(dto, Member.class));
        return addRolesToDto(mapper.map(savedMember, MemberResponseDto.class));
    }

    public void updateRoleOfMember(Member member, RoleUpdateDto role) {
        int status = role.getStatus();
        switch (status) {
            case STATUS_ADD:
                if (role.getId() != null && memberRoleRepository.existsByMemberIdAndRoleId(member.getId(), role.getId())) {
                    memberRoleRepository.deleteByMemberIdAndRoleId(member.getId(), role.getId());
                }
                Role addRole = mapper.map(roleService.addRole(role), Role.class);
                memberRoleRepository.save(MemberRole.builder().member(member).role(addRole).build());
                break;
            case STATUS_REMOVE:
                memberRoleRepository.deleteByMemberIdAndRoleId(member.getId(), role.getId());
                roleRepository.deleteById(role.getId());
                break;
            case STATUS_NONE:
            default:
                break;
        }
    }

    public void deleteMember(Long id) {
        List<MemberRole> roleList = memberRoleRepository.findAllByMemberId(id);
        for (MemberRole memberRole : roleList) {
            memberRoleRepository.deleteById(memberRole.getId());
            roleRepository.deleteById(memberRole.getRole().getId());
        }
        memberRepository.deleteById(id);
    }

    public MemberResponseDto addRolesToDto(MemberResponseDto dto) {
        List<RoleResponseDto> roles = new ArrayList<>();
        List<MemberRole> mrList = memberRoleRepository.findAllByMemberId(dto.getId());
        for (MemberRole memberRole : mrList) {
            RoleResponseDto role = mapper.map(memberRole.getRole(), RoleResponseDto.class);
            roles.add(role);
        }
        dto.setRoles(roles);
        return dto;
    }

    public List<Member> getMemberListById(List<Long> idList) {
        List<Member> res = new ArrayList<>();
        for (Long id : idList) {
            res.add(memberRepository.findById(id).get());
        }
        return res;
    }

    private List<MemberResponseDto> mapMemberList(List<Member> list) {
        return list
                .stream()
                .map(m->mapper.map(m,MemberResponseDto.class))
                .collect(Collectors.toList());
    }

}
