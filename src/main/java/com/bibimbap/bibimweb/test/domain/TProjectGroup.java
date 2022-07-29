package com.bibimbap.bibimweb.test.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@SuperBuilder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@DiscriminatorValue("byProject")
public class TProjectGroup extends TGroup {
    private String content;
}
