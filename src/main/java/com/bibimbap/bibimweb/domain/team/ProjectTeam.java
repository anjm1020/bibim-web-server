package com.bibimbap.bibimweb.domain.team;

import com.bibimbap.bibimweb.dto.team.TeamUpdateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamUpdateDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@SuperBuilder @Getter @Setter @ToString(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@DiscriminatorValue("byProject")
public class ProjectTeam extends Team {
    @Column(name = "content")
    private String content;

    @Override
    public void update(TeamUpdateDto dto) {
        super.update(dto);
        this.content = ((ProjectTeamUpdateDto) dto).getContent();
    }
}
