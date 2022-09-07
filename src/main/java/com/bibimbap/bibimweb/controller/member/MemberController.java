package com.bibimbap.bibimweb.controller.member;

import com.bibimbap.bibimweb.dto.member.*;
import com.bibimbap.bibimweb.dto.member.role.AdminMemberDto;
import com.bibimbap.bibimweb.dto.member.role.AdminMemberResponseDto;
import com.bibimbap.bibimweb.dto.member.role.HonorMemberDto;
import com.bibimbap.bibimweb.dto.member.role.HonorMemberResponseDto;
import com.bibimbap.bibimweb.service.member.MemberService;
import com.bibimbap.bibimweb.service.role.MemberRoleService;
import com.bibimbap.bibimweb.util.exception.ConflictException;
import com.bibimbap.bibimweb.util.exception.NotFoundException;
import com.bibimbap.bibimweb.util.exception.OutOfRangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/members", produces = "application/json; charset=UTF8")
public class MemberController {

    private final MemberService memberService;
    private final MemberRoleService memberRoleService;

    @PostMapping("/")
    public MemberResponseDto createMember(@RequestBody MemberCreateDto member) {
        if (memberService.isExistStudentId(member.getStudentId())) {
            throw ConflictException.MEMBER;
        }
        return memberService.createMember(member);
    }

    @PostMapping("/admin/")
    public AdminMemberResponseDto createAdminMember(@RequestBody AdminMemberDto dto) {
        if (!memberService.isExistMember(dto.getMemberId())) {
            throw NotFoundException.MEMBER;
        }
        return memberRoleService.addAdminRole(dto);
    }

    @PostMapping("/honor/")
    public HonorMemberResponseDto createHonorMember(@RequestBody HonorMemberDto dto) {
        if (!memberService.isExistMember(dto.getMemberId())) {
            throw NotFoundException.MEMBER;
        }
        return memberRoleService.addHonorRole(dto);
    }

    @GetMapping("/")
    public List<MemberResponseDto> getMemberList(Pageable pageable) {

        if (!memberService.isValidPage(pageable)) {
            throw OutOfRangeException.PAGE;
        }
        return memberService.getMemberList(pageable);
    }

    @GetMapping("/admin/")
    public List<AdminMemberResponseDto> getAdminMemberList() {
        return memberService.getAdminMemberList();
    }

    @GetMapping("/honor/")
    public List<HonorMemberResponseDto> getHonorMemberList() {
        return memberService.getHonorMemberList();
    }

    @GetMapping("/{memberId}")
    public MemberResponseDto getMemberById(@PathVariable Long memberId) {

        if (!memberService.isExistMember(memberId)) {
            throw NotFoundException.MEMBER;
        }
        return memberService.getMemberById(memberId);
    }

    @PutMapping("/")
    public MemberResponseDto updateMember(@RequestBody MemberUpdateDto member) {
        if (!memberService.isExistMember(member.getId())) {
            throw NotFoundException.MEMBER;
        }
        return memberService.updateMember(member);
    }

    @PutMapping("/admin/")
    public AdminMemberResponseDto updateAdminMember(@RequestBody AdminMemberDto dto) {
        if (!memberService.isExistMember(dto.getMemberId())) {
            throw NotFoundException.MEMBER;
        }
        return memberRoleService.updateAdminRole(dto);
    }
    @PutMapping("/honor/")
    public HonorMemberResponseDto updateHonorMember(@RequestBody HonorMemberDto dto) {
        if (!memberService.isExistMember(dto.getMemberId())) {
            throw NotFoundException.MEMBER;
        }
        return memberRoleService.updateHonorRole(dto);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId) {
        if (!memberService.isExistMember(memberId)) {
            throw NotFoundException.MEMBER;
        }
        memberService.deleteMember(memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/admin/{memberId}")
    public ResponseEntity deleteAdminMember(@PathVariable Long memberId) {
        if (!memberService.isExistMember(memberId)) {
            throw NotFoundException.MEMBER;
        }
        memberRoleService.deleteAdminRole(memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/honor/{memberId}")
    public ResponseEntity deleteHonorMember(@PathVariable Long memberId, @RequestParam String groupName) {
        if (!memberService.isExistMember(memberId)) {
            throw NotFoundException.MEMBER;
        }
        memberRoleService.deleteHonorRole(memberId, groupName);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
