package com.bibimbap.bibimweb.repository.role;

import com.bibimbap.bibimweb.domain.role.team.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
    Optional<ProjectRole> findByTeamIdAndRollName(Long teamId, String rollName);
    List<ProjectRole> findAllByTeamIdAndRollName(Long teamId, String rollName);
    List<ProjectRole> findAllByTeamId(Long teamId);
    Optional<ProjectRole> findByTeamIdAndRollNameAndMemberId(Long teamId, String rollName, Long memberId);
}
