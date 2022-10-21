package com.bibimbap.bibimweb.domain.role;

import com.bibimbap.bibimweb.domain.member.Member;
import com.bibimbap.bibimweb.domain.team.Team;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@SuperBuilder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Role  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "roll_name")
    private String rollName;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}