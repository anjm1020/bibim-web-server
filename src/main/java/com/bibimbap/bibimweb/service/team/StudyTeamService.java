package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.RoleName;
import com.bibimbap.bibimweb.domain.role.team.StudyRole;
import com.bibimbap.bibimweb.domain.team.StudyDetail;
import com.bibimbap.bibimweb.domain.team.StudyTeam;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamUpdateDto;
import com.bibimbap.bibimweb.dto.team.study.detail.AttendanceResponseDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailCreateDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailResponseDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailUpdateDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.StudyRoleRepository;
import com.bibimbap.bibimweb.repository.team.study.StudyDetailRepository;
import com.bibimbap.bibimweb.repository.team.study.StudyTeamRepository;
import com.bibimbap.bibimweb.service.role.TeamRoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyTeamService {

    private final TeamRoleService teamRoleService;
    private final TagService tagService;

    private final MemberRepository memberRepository;
    private final StudyTeamRepository studyTeamRepository;
    private final StudyRoleRepository studyRoleRepository;
    private final StudyDetailRepository studyDetailRepository;

    private final ModelMapper mapper = new ModelMapper();

    public boolean isValidPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long count = studyTeamRepository.count();
        return 0 <= pageNumber && pageNumber <= ((count - 1) / pageSize);
    }

    public boolean isNotExistTeam(Long teamId) {
        return !studyTeamRepository.existsById(teamId);
    }

    public StudyTeamResponseDto createStudyTeam(StudyTeamCreateDto dto) {
        Member leader = memberRepository.findById(dto.getLeaderId()).get();

        StudyTeam studyTeam = StudyTeam.builder()
                .groupName(dto.getGroupName())
                .gitURL(dto.getGitURL())
                .blogURL(dto.getBlogURL())
                .leader(leader)
                .build();

        StudyTeam saved = studyTeamRepository.save(studyTeam);

        teamRoleService.addStudyRole(saved, leader, RoleName.LEADER, null);

        Map<Long, Integer> groupMap = dto.getGroupNumbers();
        for (Long memberId : dto.getMembers()) {
            Member member = memberRepository.findById(memberId).get();
            teamRoleService.addStudyRole(saved, member, RoleName.MEMBER, groupMap.get(memberId));
        }

        tagService.saveTags(saved.getId(), dto.getTags());
        return makeResponseDto(studyTeam);
    }

    public StudyTeamResponseDto getStudyTeamById(Long teamId) {
        return makeResponseDto(studyTeamRepository.findById(teamId).get());
    }

    public List<StudyTeamResponseDto> getStudyTeamList(Pageable pageable, String year, String tag) {
        return studyTeamRepository.findAll(pageable).stream()
                .filter(team -> year.equals("") || year.equals(String.valueOf(team.getPeriod())))
                .filter(team -> tag.equals("") || team.getTags().stream().anyMatch(t -> t.getTag().getName().equals(tag)))
                .map(o -> makeResponseDto(o))
                .collect(Collectors.toList());
    }

    public StudyDetailResponseDto getStudyDetailById(Long detailId) {
        return makeDetailResponseDto(studyDetailRepository.findById(detailId).get());
    }

    public StudyTeamResponseDto updateStudyTeam(StudyTeamUpdateDto dto) {
        StudyTeam studyTeam = studyTeamRepository.findById(dto.getId()).get();
        studyTeam.update(dto);

        Member leader = memberRepository.findById(dto.getLeaderId()).get();
        studyTeam.setLeader(leader);
        Optional<StudyRole> leaderRole = studyRoleRepository.findByTeamIdAndRollName(dto.getId(), RoleName.LEADER.name());
        if (leaderRole.isEmpty()) {
            teamRoleService.addProjectRole(studyTeam, leader, RoleName.LEADER, "");
        } else {
            StudyRole studyRole = leaderRole.get();
            teamRoleService.updateMemberOfRole(studyRole, leader);
        }

        List<Long> members = dto.getMembers();
        for (Long id : members) {
            Member curr = memberRepository.findById(id).get();
            if (studyRoleRepository.findByTeamIdAndRollNameAndMemberId(dto.getId(), RoleName.MEMBER.name(), id).isEmpty()) {
                // 신규 멤버
                teamRoleService.addProjectRole(studyTeam, curr, RoleName.MEMBER, "");
            }
        }
        // find member to delete
        List<StudyRole> teamMembers = studyRoleRepository.findAllByTeamIdAndRollName(dto.getId(), RoleName.MEMBER.name());
        for (StudyRole pr : teamMembers) {
            if (members.stream().noneMatch(id -> pr.getMember().getId() == id)) {
                teamRoleService.deleteRole(pr);
            }
        }

        Map<Long, Integer> groupNumbers = dto.getGroupNumbers();
        for (Long memberId : groupNumbers.keySet()) {
            teamRoleService.updateStudyGroupOfTeam(dto.getId(), memberId, groupNumbers.get(memberId));
        }

        // tag update
        tagService.updateTags(studyTeam.getId(), dto.getTags());
        StudyTeam saved = studyTeamRepository.save(studyTeam);
        return makeResponseDto(saved);
    }

    public StudyDetailResponseDto addStudyDetail(StudyDetailCreateDto dto) {
        StudyTeam studyTeam = studyTeamRepository.findById(dto.getTeamId()).get();
        StudyDetail saved = studyDetailRepository.save(StudyDetail.builder()
                .studyTeam(studyTeam)
                .week(dto.getWeek())
                .content(dto.getContent())
                .build());
        studyTeam.getDetails().add(saved);

        dto.getAttendances().stream()
                .forEach(attendance -> {
                    StudyRole studyRole = studyRoleRepository.findByTeamIdAndRollNameAndMemberId(dto.getTeamId(), RoleName.MEMBER.name(), attendance.getMemberId()).get();
                    List<Boolean> attendanceList = toAttendanceList(studyRole.getAttendance());
                    if (attendance.getWeek() >= attendanceList.size()) {
                        // 2주차면 index = 1, original.size = 1
                        attendanceList.add(attendance.isAttend());
                    } else {
                        attendanceList.set(attendance.getWeek() - 1, attendance.isAttend());
                    }

                    studyRole.setAttendance(toAttendanceString(attendanceList));
                    studyRoleRepository.save(studyRole);
                });

        return makeDetailResponseDto(saved);
    }

    public StudyDetailResponseDto updateStudyDetail(StudyDetailUpdateDto dto) {
        StudyDetail detail = studyDetailRepository.findById(dto.getId()).get();
        detail.setWeek(dto.getWeek());
        detail.setContent(dto.getContent());

        dto.getAttendances().stream()
                .forEach(attendance -> {
                    StudyRole studyRole = studyRoleRepository.findByTeamIdAndRollNameAndMemberId(detail.getStudyTeam().getId(), RoleName.MEMBER.name(), attendance.getMemberId()).get();
                    List<Boolean> attendanceList = toAttendanceList(studyRole.getAttendance());

                    attendanceList.set(attendance.getWeek() - 1, attendance.isAttend());

                    studyRole.setAttendance(toAttendanceString(attendanceList));
                    studyRoleRepository.save(studyRole);
                });

        StudyDetail saved = studyDetailRepository.save(detail);
        return makeDetailResponseDto(saved);
    }

    public void deleteStudyTeam(Long teamId) {

        StudyTeam studyTeam = studyTeamRepository.findById(teamId).get();

        List<StudyRole> deleteList = new ArrayList<>();
        studyTeam.getMemberRoles()
                .forEach(role -> {
                    deleteList.add((StudyRole) role);
                });
        for (StudyRole role : deleteList) {
            teamRoleService.deleteRole(role);
        }
        // detail 지우기
        for (StudyDetail detail : studyTeam.getDetails()) {
            studyDetailRepository.delete(detail);
        }

        // tag 지워주기
        tagService.deleteAllTeamTag(teamId);
        studyTeamRepository.deleteById(teamId);
    }

    public void deleteStudyDetail(Long detailId) {
        StudyDetail studyDetail = studyDetailRepository.findById(detailId).get();
        StudyTeam studyTeam = studyDetail.getStudyTeam();
        studyTeam.getDetails().remove(studyDetail);

        // 출석 정보 삭제
        // 그냥 출석 0 처리
        studyTeam.getMemberRoles().stream()
                .filter(mr -> mr.getRollName().equals(RoleName.MEMBER.name()))
                .forEach(mr -> {
                    StudyRole studyRole = (StudyRole) mr;
                    List<Boolean> attendanceList = toAttendanceList(studyRole.getAttendance());
                    attendanceList.set(studyDetail.getWeek() - 1, false);
                    studyRole.setAttendance(toAttendanceString(attendanceList));
                    studyRoleRepository.save(studyRole);
                });

        studyDetailRepository.deleteById(detailId);
    }

    private StudyDetailResponseDto makeDetailResponseDto(StudyDetail detail) {
        StudyDetailResponseDto res = mapper.map(detail, StudyDetailResponseDto.class);
        List<AttendanceResponseDto> attendances = detail.getStudyTeam().getMemberRoles().stream()
                .filter(mr -> mr.getRollName().equals(RoleName.MEMBER.name()))
                .map(mr -> {
                    StudyRole studyRole = (StudyRole) mr;
                    Boolean isAttend = toAttendanceList(studyRole.getAttendance()).get(detail.getWeek() - 1);
                    return AttendanceResponseDto.builder()
                            .isAttend(isAttend)
                            .memberName(studyRole.getMember().getName())
                            .groupNumber(studyRole.getGroupNumber())
                            .build();
                })
                .collect(Collectors.toList());
        res.setAttendances(attendances);
        return res;
    }

    private StudyTeamResponseDto makeResponseDto(StudyTeam studyTeam) {
        StudyTeamResponseDto res = mapper.map(studyTeam, StudyTeamResponseDto.class);
        for (StudyDetailResponseDto detail : res.getDetails()) {
            List<AttendanceResponseDto> attendances = studyTeam.getMemberRoles().stream()
                    .filter(mr -> mr.getRollName().equals(RoleName.MEMBER.name()))
                    .map(mr -> {
                        StudyRole studyRole = (StudyRole) mr;
                        Boolean isAttend = toAttendanceList(studyRole.getAttendance()).get(detail.getWeek() - 1);
                        return AttendanceResponseDto.builder()
                                .isAttend(isAttend)
                                .memberName(studyRole.getMember().getName())
                                .groupNumber(studyRole.getGroupNumber())
                                .build();
                    })
                    .collect(Collectors.toList());
            detail.setAttendances(attendances);
        }
        res.setMembersAndTags(studyTeam);
        return res;
    }

    private List<Boolean> toAttendanceList(String attendanceString) {
        if (attendanceString.equals("")) return new ArrayList<>();
        return Arrays.stream(attendanceString.split(","))
                .map(str -> str.equals("1"))
                .collect(Collectors.toList());
    }

    private String toAttendanceString(List<Boolean> attendanceList) {
        StringBuilder sb = new StringBuilder();
        for (Boolean isAttend : attendanceList) {
            sb.append(isAttend ? "1" : "0").append(",");
        }
        return sb.toString();
    }

}
