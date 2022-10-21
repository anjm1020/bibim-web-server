package com.bibimbap.bibimweb.domain.member;

import com.bibimbap.bibimweb.domain.role.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @Builder @ToString
@NoArgsConstructor @AllArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "git_url")
    private String gitUrl;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    
}
