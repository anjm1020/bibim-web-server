package com.bibimbap.bibimweb.domain.role;


import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("byProject")
public class ProjectRole extends Role {
    @Column(name = "field")
    private String field;
}

