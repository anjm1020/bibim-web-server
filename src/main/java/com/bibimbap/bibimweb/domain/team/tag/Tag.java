package com.bibimbap.bibimweb.domain.team.tag;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "tag")
    List<TeamTag> teamTagList = new ArrayList<>();

    @Override
    public String toString() {
        return "Tag{" +
                "Id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
