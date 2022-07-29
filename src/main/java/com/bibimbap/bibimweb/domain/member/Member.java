package com.bibimbap.bibimweb.domain.member;

import com.bibimbap.bibimweb.domain.role.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @SuperBuilder @ToString
@NoArgsConstructor @AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String studentId;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Role> roles = new ArrayList<>();
}
