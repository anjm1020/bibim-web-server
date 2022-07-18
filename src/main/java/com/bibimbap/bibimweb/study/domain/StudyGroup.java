package com.bibimbap.bibimweb.study.domain;

import com.bibimbap.bibimweb.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class StudyGroup {

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
            name = "study_group_week",
            joinColumns = @JoinColumn(name = "study_group_id"),
            inverseJoinColumns = @JoinColumn(name = "study_week_id")
    )
    private List<StudyWeek> details;

    @OneToMany
    @JoinTable(
            name = "study_group_member",
            joinColumns = @JoinColumn(name = "study_group_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> members;
}
