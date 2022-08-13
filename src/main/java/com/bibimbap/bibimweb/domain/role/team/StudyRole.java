package com.bibimbap.bibimweb.domain.role.team;

import com.bibimbap.bibimweb.domain.role.Role;
import com.bibimbap.bibimweb.domain.team.StudyTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("byStudy")
public class StudyRole extends Role {
    @Column(name = "group_number")
    private Integer groupNumber;

    @Column(name = "attendance")
    private String attendance;
}