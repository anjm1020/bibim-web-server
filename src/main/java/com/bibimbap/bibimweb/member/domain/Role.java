package com.bibimbap.bibimweb.member.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String groupType;

    @Column
    private Long groupId;

    @Column
    private String groupName;

    @Column
    private String role;

}
