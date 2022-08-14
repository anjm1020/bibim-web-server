package com.bibimbap.bibimweb.service.member;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.dto.member.*;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.AdminRoleRepository;
import com.bibimbap.bibimweb.repository.role.HonorRoleRepository;
import com.bibimbap.bibimweb.service.role.MemberRoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AdminRoleRepository adminRoleRepository;
    private final HonorRoleRepository honorRoleRepository;
    private ModelMapper mapper = new ModelMapper();

    public boolean isExistMember(Long id) {
        return memberRepository.existsById(id);
    }

    public boolean isExistStudentId(String studentId) {
        return memberRepository.existsByStudentId(studentId);
    }

    public boolean isValidPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long count = memberRepository.count();
        return 0 <= pageNumber && pageNumber <= ((count - 1) / pageSize);
    }

    public MemberResponseDto createMember(MemberCreateDto dto) {
        Member newMember = mapper.map(dto, Member.class);
        newMember.setId(null);
        return mapper.map(memberRepository.save(newMember), MemberResponseDto.class);
    }

    public MemberResponseDto getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        return mapper.map(member, MemberResponseDto.class);
    }

    public List<MemberResponseDto> getMemberList(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .stream()
                .map(o -> mapper.map(o, MemberResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<AdminMemberResponseDto> getAdminMemberList() {
        return adminRoleRepository.findAll().stream()
                .map(role -> {
                    Member member = role.getMember();
                    AdminMemberResponseDto res = mapper.map(member, AdminMemberResponseDto.class);
                    res.setPosition(role.getPosition());
                    res.setPeriod(role.getPeriod());
                    return res;
                })
                .collect(Collectors.toList());
    }

    public List<HonorMemberResponseDto> getHonorMemberList() {
        return honorRoleRepository.findAll().stream()
                .map(role -> {
                    Member member = role.getMember();
                    HonorMemberResponseDto res = mapper.map(member, HonorMemberResponseDto.class);
                    res.setPeriod(role.getPeriod());
                    res.setGroupName(role.getGroupName());
                    return res;
                })
                .collect(Collectors.toList());
    }

    public MemberResponseDto updateMember(MemberUpdateDto dto) {
        Member newMember = mapper.map(dto, Member.class);
        return mapper.map(memberRepository.save(newMember), MemberResponseDto.class);
    }

    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

}
