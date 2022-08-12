package com.bibimbap.bibimweb.domain.team;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_detail_id")
    private Long id;

    @Column(name = "week")
    private Integer week;

    @Column(name = "content")
    private String content;
}