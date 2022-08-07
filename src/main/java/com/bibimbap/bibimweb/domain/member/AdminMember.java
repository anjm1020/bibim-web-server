package com.bibimbap.bibimweb.domain.member;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter @SuperBuilder @ToString
@NoArgsConstructor @AllArgsConstructor
@DiscriminatorValue("byAdmin")
public class AdminMember extends Member {
    private String position;
    private Long period;
}
