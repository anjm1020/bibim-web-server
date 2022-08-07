package com.bibimbap.bibimweb.domain.role;


import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("byProject")
public class ProjectRole extends Role {
    private String filed;
}

