package com.bibimbap.bibimweb.domain.team;

import com.bibimbap.bibimweb.test.domain.TGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@SuperBuilder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@DiscriminatorValue("byProject")
public class ProjectTeam extends Team {
    private String content;
}
