package com.bibimbap.bibimweb.controller.member;

import com.bibimbap.bibimweb.dto.member.*;
import com.bibimbap.bibimweb.service.member.MemberService;
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

    @PostMapping("/")
    public MemberResponseDto createMember(@RequestBody MemberCreateDto member) {
        if (memberService.isExistStudentId(member.getStudentId())) {
            throw ConflictException.MEMBER;
        }
        return memberService.createMember(member);
    }

    @GetMapping("/")
    public List<MemberResponseDto> getMemberList(Pageable pageable) {

        if (!memberService.isValidPage(pageable)) {
            throw OutOfRangeException.PAGE;
        }
        return memberService.getMemberList(pageable);
    }

    @GetMapping(value = "/", params = "admin")
    public List<AdminMemberResponseDto> getAdminMemberList() {
        return memberService.getAdminMemberList();
    }

    @GetMapping(value = "/", params = "honor")
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

    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId) {
        if (!memberService.isExistMember(memberId)) {
            throw NotFoundException.MEMBER;
        }
        memberService.deleteMember(memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
