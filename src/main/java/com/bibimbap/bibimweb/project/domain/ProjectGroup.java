package com.bibimbap.bibimweb.project.domain;

import com.bibimbap.bibimweb.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Builder @ToString
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String period;

    @Column
    private String teamName;

    @ManyToOne
    @JoinColumn
    private Member leader;

    @Column
    private String content;

    @OneToMany
    @JoinTable(
            name = "project_group_member",
            joinColumns = @JoinColumn(name = "PROJECT_GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    @Builder.Default
    private List<Member> members = new ArrayList<>();
}
