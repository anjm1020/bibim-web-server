package com.bibimbap.bibimweb.domain.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@SuperBuilder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@DiscriminatorValue("byStudy")
public class StudyTeam extends Team {
    @OneToMany
    @JoinColumn(name = "team_id")
    private List<StudyDetail> details;
}