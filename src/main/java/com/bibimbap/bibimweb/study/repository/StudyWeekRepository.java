package com.bibimbap.bibimweb.study.repository;

import com.bibimbap.bibimweb.study.domain.StudyWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyWeekRepository extends JpaRepository<StudyWeek, Long> {

}
