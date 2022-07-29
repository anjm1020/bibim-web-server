package com.bibimbap.bibimweb.domain.role;


import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @ManyToOne
    @JoinColumn
    private ProjectTeam projectGroup;
}

