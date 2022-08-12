package com.bibimbap.bibimweb.domain.team;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.team.tag.TeamTag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @SuperBuilder @ToString
@AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "period")
    private String period;

    @Column(name = "git_url")
    private String gitURL;

    @Column(name = "blog_url")
    private String blogURL;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private Member leader;

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<Role> memberRoles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<TeamTag> tags = new ArrayList<>();
}