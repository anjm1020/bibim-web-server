package com.bibimbap.bibimweb.domain.role.member;

import com.bibimbap.bibimweb.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("byStudy")
public class HonorRole extends Role {
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "period")
    private Integer period;
}
