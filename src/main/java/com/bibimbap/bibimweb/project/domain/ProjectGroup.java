package com.bibimbap.bibimweb.project.domain;

import com.bibimbap.bibimweb.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class ProjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer period;

    @Column
    private String teamName;

    @Column
    private String leaderName;

    @OneToMany
    @JoinTable(
            name = "project_group_member",
            joinColumns = @JoinColumn(name = "PROJECT_GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    private List<Member> members;
}
