package com.bibimbap.bibimweb.dto.team;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateDto {
    private String groupName;
    private String gitURL;
    private String blogURL;
    private Long leaderId;
    @Builder.Default
    private List<Long> members = new ArrayList<>();
    @Builder.Default
    private List<String> tags = new ArrayList<>();
}
