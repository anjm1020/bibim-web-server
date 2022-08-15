package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.RoleName;
import com.bibimbap.bibimweb.domain.team.StudyTeam;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamResponseDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.StudyRoleRepository;
import com.bibimbap.bibimweb.repository.team.StudyTeamRepository;
import com.bibimbap.bibimweb.service.role.TeamRoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyTeamService {

    private final TeamRoleService teamRoleService;
    private final TagService tagService;

    private final StudyTeamRepository studyTeamRepository;
    private final MemberRepository memberRepository;
    private final StudyRoleRepository studyRoleRepository;

    private final ModelMapper mapper = new ModelMapper();

    public boolean isValidPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long count = studyTeamRepository.count();
        return 0 <= pageNumber && pageNumber <= ((count - 1) / pageSize);
    }

    public boolean isNotExistTeam(Long teamId) {
        return studyTeamRepository.existsById(teamId);
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

        for (Long memberId : dto.getMembers()) {
            Member member = memberRepository.findById(memberId).get();
            teamRoleService.addStudyRole(saved, member, RoleName.MEMBER, null);
        }

        tagService.saveTags(saved.getId(), dto.getTags());
        return makeResponseDto(studyTeam);
    }

    public StudyTeamResponseDto getStudyTeamById(Long teamId) {
        return makeResponseDto(studyTeamRepository.findById(teamId).get());
    }

    public StudyTeamResponseDto updateStudyTeam() {
        return null;
    }

    private StudyTeamResponseDto makeResponseDto(StudyTeam studyTeam) {
        // 테스트 해보고 수정해야함
        StudyTeamResponseDto res = mapper.map(studyTeam, StudyTeamResponseDto.class);
        res.setMembersAndTags(studyTeam);
        return res;
    }

}
