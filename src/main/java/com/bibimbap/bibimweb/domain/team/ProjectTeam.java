package com.bibimbap.bibimweb.domain.team;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@SuperBuilder @Getter @Setter @ToString(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@DiscriminatorValue("byProject")
public class ProjectTeam extends Team {
    // 깃,블로그,주소
    private String gitURL;
    private String blogURL;
    // 활동 내역
    private String content;
}
