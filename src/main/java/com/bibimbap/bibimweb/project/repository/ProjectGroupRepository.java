package com.bibimbap.bibimweb.project.repository;

import com.bibimbap.bibimweb.project.domain.ProjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Long> {
    List<ProjectGroup> findAllByPeriod(String period);
}
