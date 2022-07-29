package com.bibimbap.bibimweb.test.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@SuperBuilder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@DiscriminatorValue("byStudy")
public class TStudyRole extends TRole {

    @ManyToOne
    @JoinColumn
    private TStudyGroup studyGroup;

}
