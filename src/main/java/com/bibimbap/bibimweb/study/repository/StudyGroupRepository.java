package com.bibimbap.bibimweb.study.repository;

import com.bibimbap.bibimweb.study.domain.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

}
