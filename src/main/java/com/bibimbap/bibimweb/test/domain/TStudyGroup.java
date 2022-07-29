package com.bibimbap.bibimweb.test.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@SuperBuilder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@DiscriminatorValue("byStudy")
public class TStudyGroup extends TGroup {

    @OneToMany
    @JoinColumn
    private List<TStudyDetail> details;
}
