package com.bibimbap.bibimweb.test.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@SuperBuilder
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class TRole  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rollName;

    @ManyToOne
    @JoinColumn
    private TMember member;
}
