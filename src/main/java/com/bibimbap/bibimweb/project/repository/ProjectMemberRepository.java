package com.bibimbap.bibimweb.project.repository;

import com.bibimbap.bibimweb.project.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
}
