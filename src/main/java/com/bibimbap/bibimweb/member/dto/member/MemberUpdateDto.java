package com.bibimbap.bibimweb.member.dto.member;

import com.bibimbap.bibimweb.member.dto.role.RoleUpdateDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MemberUpdateDto {
    private Long id;
    private String name;
    private String studentId;
    private String attendance;

    @Builder.Default
    private List<RoleUpdateDto> roles = new ArrayList<>();
}
