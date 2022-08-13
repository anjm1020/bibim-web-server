package com.bibimbap.bibimweb.domain.role.member;

import com.bibimbap.bibimweb.domain.role.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("AdminRole")
public class AdminRole extends Role {
    @Column(name = "position")
    private String position;

    @Builder.Default
    @Column(name = "periond")
    private Integer period = LocalDate.now().getYear();

}
