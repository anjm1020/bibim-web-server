package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.study.detail.AttendanceManageDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailCreateDto;
import com.bibimbap.bibimweb.repository.team.study.StudyTeamRepository;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class StudyTeamServiceTest {

    @Autowired
    StudyTeamService studyTeamService;
    @Autowired
    StudyTeamRepository studyTeamRepository;
    @Autowired
    MemberManager memberManager;

    @Test
    @DisplayName("스터디팀 생성 테스트")
    void createTeam() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "1");
        MemberResponseDto memberB = memberManager.createMember("memberB", "2");
        MemberResponseDto memberC = memberManager.createMember("memberC", "3");

        List<Long> members = new ArrayList<>();
        members.add(memberB.getId());
        members.add(memberC.getId());

        StudyTeamResponseDto team1 = studyTeamService.createStudyTeam(StudyTeamCreateDto.builder()
                .groupName("team1")
                .leaderId(memberA.getId())
                .members(members)
                .build());

        assertThat(team1.getLeader().getId()).isEqualTo(memberA.getId());
        members.forEach(memberId -> assertThat(team1.getMembers().stream()
                .anyMatch(member -> member.getId().equals(memberId))).isTrue());
    }

    @Test
    @DisplayName("주차별 활동 생성 테스트")
    void createDetail() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "1");
        MemberResponseDto memberB = memberManager.createMember("memberB", "2");
        MemberResponseDto memberC = memberManager.createMember("memberC", "3");

        List<Long> members = new ArrayList<>();
        members.add(memberB.getId());
        members.add(memberC.getId());

        Map<Long, Integer> groupMapping = new HashMap<>();
        groupMapping.put(memberB.getId(), 1);
        groupMapping.put(memberC.getId(), 2);

        StudyTeamResponseDto team1 = studyTeamService.createStudyTeam(StudyTeamCreateDto.builder()
                .groupName("team1")
                .leaderId(memberA.getId())
                .members(members)
                .groupNumbers(groupMapping)
                .build());

        List<AttendanceManageDto> attendances = new ArrayList<>();
        members.forEach(memberId -> attendances.add(AttendanceManageDto.builder()
                .week(1)
                .memberId(memberId)
                .isAttend(true)
                .build()));

        System.out.println("1주차 활동 추가");
        StudyTeamResponseDto res = studyTeamService.addStudyDetail(StudyDetailCreateDto.builder()
                .teamId(team1.getId())
                .content("1주차 활동")
                .week(1)
                .attendances(attendances)
                .build());

        List<AttendanceManageDto> attendances2 = new ArrayList<>();
        members.forEach(memberId -> attendances2.add(AttendanceManageDto.builder()
                .week(2)
                .memberId(memberId)
                .isAttend(false)
                .build()));

        System.out.println("2주차 활동 추가");
        StudyTeamResponseDto res2 = studyTeamService.addStudyDetail(StudyDetailCreateDto.builder()
                .teamId(team1.getId())
                .content("2주차 활동")
                .week(2)
                .attendances(attendances2)
                .build());
        System.out.println(res2);
    }
}
