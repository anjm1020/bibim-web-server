package com.bibimbap.bibimweb.member.domain;

import lombok.*;

import javax.persistence.*;

@Entity @Builder
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
