package com.bibimbap.bibimweb.test.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class TStudyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String detail;
}
