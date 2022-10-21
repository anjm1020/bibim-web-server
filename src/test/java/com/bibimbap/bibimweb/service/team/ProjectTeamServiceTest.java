package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.team.ProjectRole;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import com.bibimbap.bibimweb.domain.team.tag.TeamTag;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import com.bibimbap.bibimweb.dto.member.team.MemberTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamUpdateDto;
import com.bibimbap.bibimweb.repository.member.MemberRepository;
import com.bibimbap.bibimweb.repository.role.ProjectRoleRepository;
import com.bibimbap.bibimweb.repository.team.project.ProjectTeamRepository;
import com.bibimbap.bibimweb.repository.team.tag.TagRepository;
import com.bibimbap.bibimweb.repository.team.tag.TeamTagRepository;
import com.bibimbap.bibimweb.service.lib.MemberManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProjectTeamServiceTest {

    @Autowired
    ProjectTeamService projectTeamService;
    @Autowired
    ProjectTeamRepository projectTeamRepository;
    @Autowired
    TagRepository tagRepository;
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
        assertThat(saved.getPeriod()).isEqualTo(LocalDate.now().getYear());

        // Member
        MemberTeamResponseDto leader = saved.getLeader();
        assertThat(leader.getId()).isEqualTo(memberA.getId());
        assertThat(leader.getName()).isEqualTo(memberA.getName());

        List<MemberTeamResponseDto> members = saved.getMembers();
        assertThat(members.size()).isEqualTo(memberList.size());
        for (MemberTeamResponseDto member : members) {
            System.out.println(member);
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

        for (MemberTeamResponseDto member : members) {
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
        assertThat(updateProjectTeam.getPeriod()).isEqualTo(LocalDate.now().getYear());
        assertThat(updateProjectTeam.getGroupName()).isEqualTo(groupName);
        assertThat(updateProjectTeam.getContent()).isEqualTo(content);

        // Role.Team
        List<ProjectRole> roleListByTeamId = projectRoleRepository.findAllByTeamId(updateProjectTeam.getId());
        for (ProjectRole projectRole : roleListByTeamId) {
            assertThat(projectRole.getTeam().getGroupName()).isEqualTo(groupName);
            assertThat(projectRole.getTeam().getPeriod()).isEqualTo((LocalDate.now().getYear()));
        }
        // Member.Role.Team
        for (Long id : memberList) {
            Member member = memberRepository.findById(id).get();
            for (Role role : member.getRoles()) {
                assertThat(role.getTeam().getGroupName()).isEqualTo(groupName);
                assertThat(role.getTeam().getPeriod()).isEqualTo((LocalDate.now().getYear()));
            }
        }
        // TeamTag.Team
        List<TeamTag> tagListByTeamId = teamTagRepository.findAllByTeamId(updateProjectTeam.getId());
        for (TeamTag teamTag : tagListByTeamId) {
            assertThat(teamTag.getTeam().getGroupName()).isEqualTo(groupName);
            assertThat(teamTag.getTeam().getPeriod()).isEqualTo((LocalDate.now().getYear()));
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
        List<Long> newMemberList = new ArrayList<>();
        newMemberList.add(memberC.getId());
        newMemberList.add(memberD.getId());

        ProjectTeamUpdateDto updateDto = ProjectTeamUpdateDto.builder()
                .id(saved.getId())
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(newMemberList)
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

        for (Long id : newMemberList) {
            assertThat(roleList.stream()
                    .anyMatch(r -> r.getRollName().equals("MEMBER")
                            && r.getMember().getId() == id)).isTrue();
        }
        // team.roles.members 잘 제거되고 추가 되었는지
        ProjectTeam findUpdateTeam = projectTeamRepository.findById(updateProjectTeam.getId()).get();
        List<Role> updateRoles = findUpdateTeam.getMemberRoles();
        for (Role updateRole : updateRoles) {
            System.out.println(updateRole.getMember().getName() + " / "
                    + updateRole.getTeam().getGroupName() + " / "
                    + updateRole.getRollName());
        }
        assertThat(updateRoles.size()).isEqualTo(newMemberList.size() + 1);
        assertThat(updateRoles.stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getMember().getId() == oldMember.getId())).isFalse();
        assertThat(updateRoles.stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getMember().getId() == newMember.getId())).isTrue();
    }

    @Test
    @DisplayName("팀 생성 후 태그 수정 테스트")
    void updateTagList() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        String keepTag = "TAG1";
        String deleteTag = "MyTag";
        String newTag = "MyTag2";

        List<String> tagList = new ArrayList<>();
        tagList.add(keepTag);
        tagList.add(deleteTag);

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

        List<String> newTagList = new ArrayList<>();
        newTagList.add(keepTag);
        newTagList.add(newTag);

        ProjectTeamResponseDto updateProjectTeam = projectTeamService.updateProjectTeam(ProjectTeamUpdateDto.builder()
                .id(saved.getId())
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(newTagList)
                .build());

        // Team.Tags
        ProjectTeam findProjectTeam = projectTeamRepository.findById(updateProjectTeam.getId()).get();
        List<TeamTag> tags = findProjectTeam.getTags();
        assertThat(tags.size()).isEqualTo(newTagList.size());
        assertThat(tags.stream()
                .anyMatch(tag -> tag.getTag().getName().equals(deleteTag))).isFalse();
        for (TeamTag tag : tags) {
            assertThat(tag.getTeam().getId()).isEqualTo(findProjectTeam.getId());
            assertThat(newTagList.stream()
                    .anyMatch(name -> tag.getTag().getName().equals(name))).isTrue();
        }
        // Tags
        List<TeamTag> findTags = teamTagRepository.findAllByTeamId(updateProjectTeam.getId());
        for (TeamTag tTag : findTags) {
            assertThat(tTag.getTag().getTeamTagList().stream()
                    .anyMatch(t -> t.getTeam().getId() == findProjectTeam.getId())).isTrue();
        }
    }

    @Test
    @DisplayName("한 멤버에 여러 팀 가입 -> Member.Roles,Team.Roles 필드가 제대로 나온는지 테스트")
    void oneMemberMultipleTeam() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");
        MemberResponseDto memberD = memberManager.createMember("memberD", "333");

        List<Long> team1MemberList = new ArrayList<>();
        team1MemberList.add(memberB.getId());
        team1MemberList.add(memberC.getId());

        String keepTag = "TAG1";
        String deleteTag = "MyTag";

        List<String> tagList = new ArrayList<>();
        tagList.add(keepTag);
        tagList.add(deleteTag);

        String groupName = "team1";
        String content = "Project Team";

        ProjectTeamCreateDto team1Dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(team1MemberList)
                .tags(tagList)
                .build();

        List<Long> team2MemberList = new ArrayList<>();
        team2MemberList.add(memberA.getId());
        team2MemberList.add(memberC.getId());
        team2MemberList.add(memberD.getId());

        ProjectTeamCreateDto team2Dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberB.getId())
                .content(content)
                .members(team2MemberList)
                .tags(tagList)
                .build();

        ProjectTeamResponseDto team1 = projectTeamService.createProjectTeam(team1Dto);
        ProjectTeamResponseDto team2 = projectTeamService.createProjectTeam(team2Dto);

        // A 1L , 2M
        // B 1M , 2L
        // C 1M , 2M
        // D 2M
        // member.roles
        List<Role> aRoles = memberRepository.findById(memberA.getId()).get().getRoles();
        assertThat(aRoles.size()).isEqualTo(2);
        assertThat(aRoles.stream()
                .anyMatch(r -> r.getRollName().equals("LEADER")
                        && r.getTeam().getId() == team1.getId()
                        && r.getMember().getId() == memberA.getId())).isTrue();
        assertThat(aRoles.stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getTeam().getId() == team2.getId()
                        && r.getMember().getId() == memberA.getId())).isTrue();
        List<Role> bRoles = memberRepository.findById(memberB.getId()).get().getRoles();
        assertThat(bRoles.size()).isEqualTo(2);
        assertThat(bRoles.stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getTeam().getId() == team1.getId()
                        && r.getMember().getId() == memberB.getId())).isTrue();
        assertThat(bRoles.stream()
                .anyMatch(r -> r.getRollName().equals("LEADER")
                        && r.getTeam().getId() == team2.getId()
                        && r.getMember().getId() == memberB.getId())).isTrue();
        List<Role> cRoles = memberRepository.findById(memberC.getId()).get().getRoles();
        assertThat(cRoles.size()).isEqualTo(2);
        assertThat(cRoles.stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getTeam().getId() == team1.getId()
                        && r.getMember().getId() == memberC.getId())).isTrue();
        assertThat(cRoles.stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getTeam().getId() == team2.getId()
                        && r.getMember().getId() == memberC.getId())).isTrue();
        List<Role> dRoles = memberRepository.findById(memberD.getId()).get().getRoles();
        assertThat(dRoles.size()).isEqualTo(1);
        assertThat(dRoles.stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getTeam().getId() == team2.getId()
                        && r.getMember().getId() == memberD.getId())).isTrue();

        // team.roles
        ProjectTeam findTeam1 = projectTeamRepository.findById(team1.getId()).get();
        List<Role> team1Roles = findTeam1.getMemberRoles();
        assertThat(team1Roles.size()).isEqualTo(team1MemberList.size() + 1);
        for (Role r : team1Roles) {
            assertThat(r.getTeam().getId()).isEqualTo(team1.getId());
            if (r.getRollName() == "LEADER") {
                assertThat(r.getMember().getId()).isEqualTo(memberA.getId());
            } else {
                assertThat(team1MemberList.stream().anyMatch(id -> r.getMember().getId() == id)).isTrue();
            }
        }

        ProjectTeam findTeam2 = projectTeamRepository.findById(team2.getId()).get();
        List<Role> team2Roles = findTeam2.getMemberRoles();
        assertThat(team2Roles.size()).isEqualTo(team2MemberList.size() + 1);
        for (Role r : team2Roles) {
            assertThat(r.getTeam().getId()).isEqualTo(team2.getId());
            if (r.getRollName() == "LEADER") {
                assertThat(r.getMember().getId()).isEqualTo(memberB.getId());
            } else {
                assertThat(team2MemberList.stream().anyMatch(id -> r.getMember().getId() == id)).isTrue();
            }
        }
    }

    @Test
    @DisplayName("팀 삭제 시 Member.Roles 가 제대로 변경 되는지 테스트")
    void checkMemberRoleAfterDelete() {
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

        projectTeamService.deleteProjectTeam(saved.getId());

        Member findMemberA = memberRepository.findById(memberA.getId()).get();
        Member findMemberB = memberRepository.findById(memberB.getId()).get();
        Member findMemberC = memberRepository.findById(memberC.getId()).get();

        assertThat(findMemberA.getRoles().stream()
                .anyMatch(r -> r.getRollName().equals("LEADER")
                        && r.getMember().getId() == findMemberA.getId()
                        && r.getTeam().getId() == saved.getId())).isFalse();
        assertThat(findMemberB.getRoles().stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getMember().getId() == findMemberA.getId()
                        && r.getTeam().getId() == saved.getId())).isFalse();
        assertThat(findMemberC.getRoles().stream()
                .anyMatch(r -> r.getRollName().equals("MEMBER")
                        && r.getMember().getId() == findMemberA.getId()
                        && r.getTeam().getId() == saved.getId())).isFalse();

        List<ProjectRole> roles = projectRoleRepository.findAllByTeamId(saved.getId());
        assertThat(roles.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("팀 삭제 시 TeamTag 가 제대로 보존 or 삭제 되는지 테스트")
    void checkTagAfterDelete() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        tagList.add("TAG1");
        tagList.add("MyTag");

        List<String> tagList2 = new ArrayList<>();
        tagList2.add("TAG1");

        String groupName = "team1";
        String content = "Project Team";

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        ProjectTeamCreateDto dto2 = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList2)
                .build();

        ProjectTeamResponseDto saved = projectTeamService.createProjectTeam(dto);
        ProjectTeamResponseDto saved2 = projectTeamService.createProjectTeam(dto2);

        projectTeamService.deleteProjectTeam(saved.getId());

        List<TeamTag> teamTags = teamTagRepository.findAllByTeamId(saved.getId());
        assertThat(teamTags.size()).isEqualTo(0);

        assertThat(tagRepository.findByName("TAG1").isPresent()).isTrue();
        assertThat(tagRepository.findByName("MyTag").isPresent()).isFalse();
    }

    @Test
    @DisplayName("페이지네이션을 통한 팀 리스트 불러오기 테스트")
    void getListByPage() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        String TAG1 = "TAG1";
        tagList.add(TAG1);
        String TAG2 = "MyTag";
        tagList.add(TAG2);

        String groupName = "team1";
        String content = "Project Team";

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();
        Long firstId = 1L;
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Long saved = projectTeamService.createProjectTeam(dto).getId();
            idList.add(saved);
            if (i == 0) {
                firstId = saved;
            }
        }

        // pagination
        int page, size;
        page = 5;
        size = 10;
        List<ProjectTeamResponseDto> list1 = projectTeamService.getProjectTeamList(PageRequest.of(page, size), "", "");
        assertThat(list1.size()).isEqualTo(size);
        assertThat(list1.get(0).getId()).isEqualTo(firstId + page * size);
        assertThat(list1.get(list1.size() - 1).getId()).isEqualTo(firstId + (page + 1) * size - 1);
    }

    @Test
    @DisplayName("태그를 통해 팀 리스트 불러오기 테스트")
    void getListByTag() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        String TAG1 = "TAG1";
        tagList.add(TAG1);
        String TAG2 = "MyTag";
        tagList.add(TAG2);

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
        String TAG3 = "MyTag2";
        tagList.add(TAG3);

        ProjectTeamCreateDto dto2 = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();
        ProjectTeamResponseDto saved2 = projectTeamService.createProjectTeam(dto2);

        List<ProjectTeamResponseDto> list1 = projectTeamService.getProjectTeamList(PageRequest.of(0, 5), "", TAG1);
        assertThat(list1.size()).isEqualTo(2);
        list1.forEach(team -> assertThat(team.getTags()).contains(TAG2));

        List<ProjectTeamResponseDto> list2 = projectTeamService.getProjectTeamList(PageRequest.of(0, 5), "", TAG2);
        assertThat(list2.size()).isEqualTo(2);
        list2.forEach(team -> assertThat(team.getTags()).contains(TAG2));

        List<ProjectTeamResponseDto> list3 = projectTeamService.getProjectTeamList(PageRequest.of(0, 5), "", TAG3);
        assertThat(list3.size()).isEqualTo(1);
        list3.forEach(team -> assertThat(team.getTags()).contains(TAG3));

    }

    @Test
    @DisplayName("년도를 통해 팀 리스트 불러오기 테스트")
    void getListByYear() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        String TAG1 = "TAG1";
        tagList.add(TAG1);
        String TAG2 = "MyTag";
        tagList.add(TAG2);

        String groupName = "team1";
        String content = "Project Team";

        ProjectTeamCreateDto dto = ProjectTeamCreateDto.builder()
                .groupName(groupName)
                .leaderId(memberA.getId())
                .content(content)
                .members(memberList)
                .tags(tagList)
                .build();

        int period2022Count = 5;
        for (int i = 0; i < period2022Count; i++) {
            projectTeamService.createProjectTeam(dto);
        }

        int period2021Count = 3;
        String period2021GroupName = "groupName2021";
        for (int i = 0; i < period2021Count; i++) {
            projectTeamRepository.save(ProjectTeam.builder()
                    .groupName(period2021GroupName)
                    .period(2021)
                    .build());
        }

        List<ProjectTeamResponseDto> list2022 = projectTeamService.getProjectTeamList(PageRequest.of(0, 10), "2022", "");
        assertThat(list2022.size()).isEqualTo(period2022Count);
        list2022.forEach(team -> assertThat(team.getGroupName().equals(groupName)));
        System.out.println("2022");
        for (ProjectTeamResponseDto projectTeamResponseDto : list2022) {
            System.out.println(projectTeamResponseDto.getGroupName()+" / "+projectTeamResponseDto.getId());
        }

        List<ProjectTeamResponseDto> list2021 = projectTeamService.getProjectTeamList(PageRequest.of(0, 10), "2021", "");
        assertThat(list2021.size()).isEqualTo(period2021Count);
        list2021.forEach(team -> assertThat(team.getGroupName().equals(period2021GroupName)));
        System.out.println("2021");
        for (ProjectTeamResponseDto projectTeamResponseDto : list2021) {
            System.out.println(projectTeamResponseDto.getGroupName()+" / "+projectTeamResponseDto.getId());
        }
    }


    @Test
    @DisplayName("단일 id로 팀 조회 테스트")
    void getTeamById() {
        MemberResponseDto memberA = memberManager.createMember("memberA", "111");
        MemberResponseDto memberB = memberManager.createMember("memberB", "222");
        MemberResponseDto memberC = memberManager.createMember("memberC", "333");

        List<Long> memberList = new ArrayList<>();
        memberList.add(memberB.getId());
        memberList.add(memberC.getId());

        List<String> tagList = new ArrayList<>();
        String TAG1 = "TAG1";
        tagList.add(TAG1);
        String TAG2 = "MyTag";
        tagList.add(TAG2);

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

        ProjectTeamResponseDto res = projectTeamService.getProjectTeamById(saved.getId());
        assertThat(res.getId()).isEqualTo(saved.getId());
        res.getMembers().forEach(member->assertThat(memberList.contains(member.getId())).isTrue());
        res.getTags().forEach(tag -> assertThat(tagList.contains(tag)).isTrue());

    }
}
