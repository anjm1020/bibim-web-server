package com.bibimbap.bibimweb.dto.member;

import com.sun.istack.NotNull;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberCreateDto {
    @NotNull
    private String name;
    @NotNull
    private String studentId;
    private String phoneNumber;
    private String email;
    private String gitUrl;
}
