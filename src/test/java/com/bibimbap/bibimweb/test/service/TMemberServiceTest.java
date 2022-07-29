package com.bibimbap.bibimweb.test.service;

import com.bibimbap.bibimweb.test.domain.*;
import com.bibimbap.bibimweb.test.repository.TMemberRepository;
import com.bibimbap.bibimweb.test.repository.group.TGroupRepository;
import com.bibimbap.bibimweb.test.repository.group.TProjectGroupRepository;
import com.bibimbap.bibimweb.test.repository.group.TStudyGroupRepository;
import com.bibimbap.bibimweb.test.repository.role.TProjectRoleRepository;
import com.bibimbap.bibimweb.test.repository.role.TRoleRepository;
import com.bibimbap.bibimweb.test.repository.role.TStudyRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class TMemberServiceTest {

    @Autowired
    TGroupRepository groupRepository;
    @Autowired
    TStudyGroupRepository studyGroupRepository;
    @Autowired
    TProjectGroupRepository projectGroupRepository;

    @Autowired
    TRoleRepository roleRepository;
    @Autowired
    TStudyRoleRepository studyRoleRepository;
    @Autowired
    TProjectRoleRepository projectRoleRepository;

    @Autowired
    TMemberRepository tMemberRepository;

    @Test
    @DisplayName("시나리오 테스트")
    public void scenario() {

        TMember memberA = tMemberRepository.save(TMember.builder()
                .name("memberA")
                .studentId("111")
                .build());
        TMember memberB = tMemberRepository.save(TMember.builder()
                .name("memberB")
                .studentId("222")
                .build());

        List<TMember> members = new ArrayList<>();
        members.add(memberA);
        members.add(memberB);

        TProjectGroup team1 = projectGroupRepository.save(TProjectGroup.builder()
                .groupName("team1")
                .leader(memberA)
                .content("team1's project")
                .members(members)
                .build());

        TStudyGroup team2 = studyGroupRepository.save(TStudyGroup.builder()
                .groupName("team2")
                .leader(memberB)
                .members(members)
                .build());

        TProjectRole team1Leader = projectRoleRepository.save(TProjectRole.builder()
                .member(memberA)
                .projectGroup(team1)
                .rollName("팀장")
                .build());

        TProjectRole team1Member = projectRoleRepository.save(TProjectRole.builder()
                .member(memberB)
                .projectGroup(team1)
                .rollName("팀원")
                .build());

        TStudyRole team2Leader = studyRoleRepository.save(TStudyRole.builder()
                .member(memberB)
                .studyGroup(team2)
                .rollName("팀장")
                .build());

        TStudyRole team2Member = studyRoleRepository.save(TStudyRole.builder()
                .member(memberA)
                .studyGroup(team2)
                .rollName("팀원")
                .build());

        List<TRole> all = roleRepository.findAll();

        TProjectGroup found1 = projectGroupRepository.findById(team1.getId()).get();

        for (TMember member : found1.getMembers()) {
            System.out.println(member);
        }

    }
}