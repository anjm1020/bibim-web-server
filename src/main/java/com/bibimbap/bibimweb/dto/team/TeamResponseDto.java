package com.bibimbap.bibimweb.dto.team;

import com.bibimbap.bibimweb.domain.team.Team;
import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponseDto {
    private Long id;
    private String groupName;
    private Integer period;
    private String gitURL;
    private String blogURL;

    @Builder.Default
    private List<MemberResponseDto> members = new ArrayList<>();
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    public void setMembersAndTags(Team team) {
        ModelMapper mapper = new ModelMapper();
        this.setMembers(team.getMemberRoles().stream()
                .filter(role -> role.getRollName().equals("MEMBER"))
                .map(role -> mapper.map(role.getMember(), MemberResponseDto.class))
                .collect(Collectors.toList()));
        this.setTags(team.getTags().stream().map(tag -> tag.getTag().getName()).collect(Collectors.toList()));
    }
}
