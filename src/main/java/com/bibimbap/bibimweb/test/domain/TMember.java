package com.bibimbap.bibimweb.test.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @Builder @ToString
@NoArgsConstructor @AllArgsConstructor
public class TMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String studentId;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<TRole> roles = new ArrayList<>();

}
