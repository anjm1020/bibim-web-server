package com.bibimbap.bibimweb.domain.team;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "createAt")
    @Builder.Default
    private LocalDate createAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "team_id")
    private StudyTeam studyTeam;
}