package com.bibimbap.bibimweb.domain.team.tag;

import com.bibimbap.bibimweb.domain.team.Team;
import com.bibimbap.bibimweb.domain.team.tag.Tag;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
public class TeamTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Tag tag;

    @ManyToOne
    @JoinColumn
    private Team team;

    public void setTag(Tag tag) {
        this.tag = tag;
        tag.getTeamTagList().add(this);
    }

    public void setTeam(Team team) {
        this.team = team;
        team.getTags().add(this);
    }

    @Override
    public String toString() {
        return "TeamTag{" +
                "id=" + id +
                ", tag=" + tag.getName() +
                ", team=" + team.getGroupName() +
                '}';
    }
}
