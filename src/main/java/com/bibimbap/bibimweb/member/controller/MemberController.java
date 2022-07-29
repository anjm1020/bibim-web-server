package com.bibimbap.bibimweb.member.controller;

import com.bibimbap.bibimweb.member.dto.member.MemberCreateDto;
import com.bibimbap.bibimweb.member.dto.member.MemberUpdateDto;
import com.bibimbap.bibimweb.member.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.member.service.RoleService;
import com.bibimbap.bibimweb.util.exception.ConflictException;
import com.bibimbap.bibimweb.util.exception.NotFoundException;
import com.bibimbap.bibimweb.util.exception.OutOfRangeException;
import com.bibimbap.bibimweb.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/members",produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final RoleService roleService;

    @PostMapping("/")
    public MemberResponseDto createMember(@RequestBody MemberCreateDto member) {
        if (memberService.isExistStudent(member.getStudentId())) {
            throw ConflictException.MEMBER;
        }
        return memberService.createMember(member);
    }


    @GetMapping(value = "/")
    public List<MemberResponseDto> getMemberList(Pageable pageable) {
        if (!memberService.isValidPage(pageable)) {
            throw OutOfRangeException.PAGE;
        }
        return memberService.getMemberList(pageable);
    }

    @GetMapping(value = "/", params = {"groupName","role"})
    public List<MemberResponseDto> getMemberListByRole(@RequestParam String groupName, @RequestParam String role) {
        if (!roleService.isExistRole(groupName,role)) {
            throw NotFoundException.ROLE;
        }
        return memberService.getMemberListByRole(groupName,role);
    }

    @GetMapping("/{id}")
    public MemberResponseDto getMemberById(@PathVariable Long id) {
        if (!memberService.isExistMember(id)) {
            throw NotFoundException.MEMBER;
        }
        return memberService.getMemberById(id);
    }

    @PutMapping("/")
    public MemberResponseDto updateMember(@RequestBody MemberUpdateDto member) {
        if (!memberService.isExistMember(member.getId())) {
            throw NotFoundException.MEMBER;
        }
        return memberService.updateMember(member);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMemberById(@PathVariable Long id) {
        if (!memberService.isExistMember(id)) {
            throw NotFoundException.MEMBER;
        }
        memberService.deleteMember(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
