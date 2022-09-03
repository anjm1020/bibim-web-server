package com.bibimbap.bibimweb.dto.team;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TeamReviewResponseDto {
    private Long id;
    private String groupName;
    private Integer period;
    private String gitURL;
    private String blogURL;
}
