package com.bibimbap.bibimweb.dto.member;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberResponseDto extends MemberResponseDto {
    private Integer period;
    private String position;
}
