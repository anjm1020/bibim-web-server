package com.bibimbap.bibimweb.domain.member;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("byHonor")
public class Honor extends Member{
    @Builder.Default
    @Column(name = "period")
    private Integer period = LocalDate.now().getYear();
}
