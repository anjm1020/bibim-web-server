package com.bibimbap.bibimweb.domain.team;

import com.bibimbap.bibimweb.domain.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter @SuperBuilder @ToString
@AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    @ManyToOne
    @JoinColumn
    private Member leader;

    private String period;

    @OneToMany
    @JoinColumn
    private List<Member> members;
}