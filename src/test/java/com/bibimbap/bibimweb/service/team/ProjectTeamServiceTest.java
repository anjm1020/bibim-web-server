package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.ProjectRole;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import com.bibimbap.bibimweb.domain.team.tag.TeamTag;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamUpdateDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.ProjectRoleRepository;
import com.bibimbap.bibimweb.repository.team.ProjectTeamRepository;
import com.bibimbap.bibimweb.repository.team.tag.TeamTagRepository;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import com.bibimbap.bibimweb.service.role.ProjectRoleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProjectTeamServiceTest {

    @Autowired
    ProjectTeamService projectTeamService;
    @Autowired
    ProjectTeamRepository projectTeamRepository;
    @Autowired
    TeamTagRepository teamTagRepository;
    @Autowired
    ProjectRoleRepository projectRoleRepository;
    @Autowired
    MemberManager memberManager;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("팀 생성 테스트")
    void createTeam() {

        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        tagList.add("TAG1");
        tagList.add("MyTag");

        String groupName = "team1";
        String content = "Project Team";

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        ProjectTeamResponseDto saved = projectTeamService.createProjectTeam(dto);

        // ProjectTeam
        assertThat(saved.getGroupName()).isEqualTo(groupName);
        assertThat(saved.getContent()).isEqualTo(content);
        assertThat(saved.getPeriod()).isEqualTo(String.valueOf(LocalDate.now().getYear()));

        // Member
        MemberResponseDto leader = saved.getLeader();
        assertThat(leader.getId()).isEqualTo(memberA.getId());
        assertThat(leader.getName()).isEqualTo(memberA.getName());

        List<MemberResponseDto> members = saved.getMembers();
        assertThat(members.size()).isEqualTo(memberList.size());
        for (MemberResponseDto member : members) {
            assertThat(memberList.stream().anyMatch(id -> member.getId() == id)).isTrue();
        }
        // Role
        List<ProjectRole> roles = projectRoleRepository.findAllByTeamId(saved.getId());
        assertThat(roles.size()).isEqualTo(memberList.size() + 1);
        for (ProjectRole role : roles) {
            if (role.getRollName() == "LEADER") {
                assertThat(role.getMember().getId()).isEqualTo(memberA.getId());
                assertThat(role.getRollName()).isEqualTo("LEADER");
            } else {
                assertThat(memberList.stream()
                        .anyMatch(id -> role.getMember().getId() == id)).isTrue();
                assertThat(role.getRollName()).isEqualTo("MEMBER");
            }
            assertThat(role.getTeam().getId()).isEqualTo(saved.getId());
        }

        Member findLeader = memberRepository.findById(leader.getId()).get();
        List<Role> leaderRoles = findLeader.getRoles();
        assertThat(leaderRoles.size()).isEqualTo(1);
        Role leaderRole = leaderRoles.get(leaderRoles.size() - 1);
        assertThat(leaderRole.getRollName()).isEqualTo("LEADER");
        assertThat(leaderRole.getMember().getId()).isEqualTo(leader.getId());
        assertThat(leaderRole.getTeam().getId()).isEqualTo(saved.getId());

        for (MemberResponseDto member : members) {
            Member curr = memberRepository.findById(member.getId()).get();
            List<Role> memberRoles = curr.getRoles();
            assertThat(memberRoles.size()).isEqualTo(1);
            Role memberRole = memberRoles.get(memberRoles.size() - 1);
            assertThat(memberRole.getRollName()).isEqualTo("MEMBER");
            assertThat(memberRole.getTeam().getId()).isEqualTo(saved.getId());
            assertThat(memberRole.getMember().getId()).isEqualTo(member.getId());
        }
        // Tag
        List<TeamTag> teamTags = teamTagRepository.findAllByTeamId(saved.getId());
        assertThat(teamTags.size()).isEqualTo(tagList.size());
        for (TeamTag teamTag : teamTags) {
            assertThat(teamTag.getTeam().getId()).isEqualTo(saved.getId());
            assertThat(tagList.stream()
                    .anyMatch(t -> t.equals(teamTag.getTag().getName()))).isTrue();
        }

        ProjectTeam findSaved = projectTeamRepository.findById(saved.getId()).get();
        List<TeamTag> tags = findSaved.getTags();
        assertThat(tags.size()).isEqualTo(tagList.size());
        for (TeamTag tag : tags) {
            assertThat(tag.getTeam().getId()).isEqualTo(saved.getId());
            assertThat(tagList.stream().anyMatch(str -> str.equals(tag.getTag().getName()))).isTrue();
        }

    }

    @Test
    @DisplayName("팀 생성후 일반 필드 수정 테스트")
    void updateNormField() {

        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        tagList.add("TAG1");
        tagList.add("MyTag");

        String groupName = "team1";
        String content = "Project Team";

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        ProjectTeamResponseDto saved = projectTeamService.createProjectTeam(dto);

        groupName = "UpdateTeam1";
        content = "UpdateContent";

        ProjectTeamUpdateDto updateDto = ProjectTeamUpdateDto.builder()
                .id(saved.getId())
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        ProjectTeamResponseDto updateProjectTeam = projectTeamService.updateProjectTeam(updateDto);

        assertThat(updateProjectTeam.getId()).isEqualTo(saved.getId());
        assertThat(updateProjectTeam.getPeriod()).isEqualTo(String.valueOf(LocalDate.now().getYear()));
        assertThat(updateProjectTeam.getGroupName()).isEqualTo(groupName);
        assertThat(updateProjectTeam.getContent()).isEqualTo(content);

        // Role.Team
        List<ProjectRole> roleListByTeamId = projectRoleRepository.findAllByTeamId(updateProjectTeam.getId());
        for (ProjectRole projectRole : roleListByTeamId) {
            assertThat(projectRole.getTeam().getGroupName()).isEqualTo(groupName);
            assertThat(projectRole.getTeam().getPeriod()).isEqualTo(String.valueOf(LocalDate.now().getYear()));
        }
        // Member.Role.Team
        for (Long id : memberList) {
            Member member = memberRepository.findById(id).get();
            for (Role role : member.getRoles()) {
                assertThat(role.getTeam().getGroupName()).isEqualTo(groupName);
                assertThat(role.getTeam().getPeriod()).isEqualTo(String.valueOf(LocalDate.now().getYear()));
            }
        }
        // TeamTag.Team
        List<TeamTag> tagListByTeamId = teamTagRepository.findAllByTeamId(updateProjectTeam.getId());
        for (TeamTag teamTag : tagListByTeamId) {
            assertThat(teamTag.getTeam().getGroupName()).isEqualTo(groupName);
            assertThat(teamTag.getTeam().getPeriod()).isEqualTo(String.valueOf(LocalDate.now().getYear()));
        }
    }

    @Test
    @DisplayName("팀 생성 후 리더 수정 테스트")
    void updateLeader() {

        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        tagList.add("TAG1");
        tagList.add("MyTag");

        String groupName = "team1";
        String content = "Project Team";

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        ProjectTeamResponseDto saved = projectTeamService.createProjectTeam(dto);

        MemberResponseDto memberD = memberManager.createMember("memberD", "444");

        ProjectTeamUpdateDto updateDto = ProjectTeamUpdateDto.builder()
                .id(saved.getId())
                .groupName(groupName)
                .leaderId(memberD.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        ProjectTeamResponseDto updateProjectTeam = projectTeamService.updateProjectTeam(updateDto);

        // Team.Leader == newLeader
        ProjectTeam findProjectTeam = projectTeamRepository.findById(updateProjectTeam.getId()).get();
        assertThat(findProjectTeam.getLeader().getId()).isEqualTo(memberD.getId());
        // oldLeader.Roles not contain leader role
        Member oldLeader = memberRepository.findById(memberA.getId()).get();
        assertThat(oldLeader.getRoles().stream()
                .anyMatch(r -> r.getTeam().getId() == updateProjectTeam.getId()
                        && r.getRollName().equals("LEADER"))).isFalse();
        // newLeader.Role contain leader role
        Member newLeader = memberRepository.findById(memberD.getId()).get();
        assertThat(newLeader.getRoles().stream()
                .anyMatch(r -> r.getTeam().getId() == updateProjectTeam.getId()
                        && r.getRollName().equals("LEADER"))).isTrue();
        // roleList -> newLeader role add
        List<ProjectRole> roleList = projectRoleRepository.findAllByTeamId(updateProjectTeam.getId());
        assertThat(roleList.stream()
                .anyMatch(r -> r.getTeam().getId() == updateProjectTeam.getId()
                        && r.getRollName().equals("LEADER")
                        && r.getMember().getId() == newLeader.getId())).isTrue();
        assertThat(roleList.stream()
                .anyMatch(r -> r.getTeam().getId() == updateProjectTeam.getId()
                        && r.getRollName().equals("LEADER")
                        && r.getMember().getId() == oldLeader.getId())).isFalse();
    }

    @Test
    @DisplayName("팀 생성 후 멤버리스트 수정 테스트")
    void updateMemberList() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        tagList.add("TAG1");
        tagList.add("MyTag");

        String groupName = "team1";
        String content = "Project Team";

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        ProjectTeamResponseDto saved = projectTeamService.createProjectTeam(dto);

        List<ProjectRole> before = projectRoleRepository.findAllByTeamId(saved.getId());
        System.out.println("BEFORE");
        for (ProjectRole projectRole : before) {
            System.out.println(projectRole.getMember().getName() + " / "
                    + projectRole.getTeam().getGroupName() + " / "
                    + projectRole.getRollName());
        }
        MemberResponseDto memberD = memberManager.createMember("memberD", "444");
        memberList.remove(memberB.getId());
        memberList.add(memberD.getId());

        ProjectTeamUpdateDto updateDto = ProjectTeamUpdateDto.builder()
                .id(saved.getId())
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        ProjectTeamResponseDto updateProjectTeam = projectTeamService.updateProjectTeam(updateDto);

        // memberB.roles 제거되었는지
        Member oldMember = memberRepository.findById(memberB.getId()).get();
        assertThat(oldMember.getRoles().stream()
                .anyMatch(r -> r.getTeam().getId() == updateProjectTeam.getId()
                        && r.getRollName().equals("MEMBER"))).isFalse();
        // memberD.roles 추가되었는지
        Member newMember = memberRepository.findById(memberD.getId()).get();
        assertThat(newMember.getRoles().stream()
                .anyMatch(r -> r.getTeam().getId() == updateProjectTeam.getId()
                        && r.getRollName().equals("MEMBER"))).isTrue();
        // roleList 잘 나오는지
        List<ProjectRole> roleList = projectRoleRepository.findAllByTeamId(updateProjectTeam.getId());
        assertThat(roleList.stream()
                .anyMatch(r -> r.getTeam().getId() == updateProjectTeam.getId()
                        && r.getRollName().equals("MEMBER")
                        && r.getMember().getId() == oldMember.getId())).isFalse();
        System.out.println("AFTER");
        for (ProjectRole projectRole : roleList) {
            System.out.println(projectRole.getMember().getName() + " / "
                    + projectRole.getTeam().getGroupName() + " / "
                    + projectRole.getRollName());
        }
        for (Long id : memberList) {
            assertThat(roleList.stream()
                    .anyMatch(r -> r.getRollName().equals("MEMBER")
                            && r.getMember().getId() == id)).isTrue();
        }
        // team.roles.members 잘 제거되고 추가 되었는지
        ProjectTeam findUpdateTeam = projectTeamRepository.findById(updateProjectTeam.getId()).get();
        List<Role> updateRoles = findUpdateTeam.getRoles();
        assertThat(updateRoles.size()).isEqualTo(memberList.size());
        assertThat(updateRoles.stream()
                .anyMatch(r -> r.getMember().getId() == oldMember.getId())).isFalse();
        assertThat(updateRoles.stream()
                .anyMatch(r -> r.getMember().getId() == newMember.getId())).isTrue();

    }

    @Test
    @DisplayName("팀 생성 후 태그 수정 테스트")
    void updateTagList() {

    }

    @Test
    @DisplayName("한 멤버에 여러 팀 가입 -> Member.Roles,Team.Roles 필드가 제대로 나온는지 테스트")
    void oneMemberMultipleTeam() {

    }

    @Test
    @DisplayName("페이지네이션을 통한 팀 리스트 불러오기 테스트")
    void getListByPage() {

    }

    @Test
    @DisplayName("태그를 통해 팀 리스트 불러오기 테스트")
    void getListByTag() {

    }

    @Test
    @DisplayName("단일 id로 팀 조회 테스트")
    void getTeamById() {

    }

    @Test
    @DisplayName("팀 삭제 시 Member.Roles 가 제대로 변경 되는지 테스트")
    void checkMemberRoleAfterDelete() {

    }

    @Test
    @DisplayName("팀 삭제 시 TeamTag 가 제대로 보존 or 삭제 되는지 테스트")
    void checkTagAfterDelete() {

    }
}