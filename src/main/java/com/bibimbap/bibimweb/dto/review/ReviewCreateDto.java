package com.bibimbap.bibimweb.dto.review;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ReviewCreateDto {
    private String content;
    private Long teamId;
    private Long memberId;
}
