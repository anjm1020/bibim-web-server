package com.bibimbap.bibimweb.dto.member;

import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;
    private String studentId;
    private String phoneNumber;
    private String email;
    private String gitUrl;
}
