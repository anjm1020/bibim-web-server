package com.bibimbap.bibimweb.test.repository;

import com.bibimbap.bibimweb.test.domain.TMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TMemberRepository extends JpaRepository<TMember,Long> {

}
