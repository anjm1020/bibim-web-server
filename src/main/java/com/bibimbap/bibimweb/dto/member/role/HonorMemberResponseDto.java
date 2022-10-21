package com.bibimbap.bibimweb.dto.member.role;

import com.bibimbap.bibimweb.dto.member.MemberResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class HonorMemberResponseDto extends MemberResponseDto {
    private Integer period;
    private String groupName;
}
