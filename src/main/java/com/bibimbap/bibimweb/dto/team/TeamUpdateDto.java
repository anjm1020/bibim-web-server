package com.bibimbap.bibimweb.dto.team;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdateDto {
    private Long id;
    private String groupName;
    private String gitURL;
    private String blogURL;
    private Long leaderId;
    @Builder.Default
    private List<Long> members = new ArrayList<>();
    @Builder.Default
    private List<String> tags = new ArrayList<>();
}
