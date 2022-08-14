package com.bibimbap.bibimweb.dto.team;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter @Setter @Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class TeamResponseDto {
    private Long id;
    private String groupName;
    private Integer period;
    private String gitURL;
    private String blogURL;
}
