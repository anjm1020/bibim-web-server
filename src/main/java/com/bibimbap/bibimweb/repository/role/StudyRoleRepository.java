package com.bibimbap.bibimweb.repository.role;

import com.bibimbap.bibimweb.domain.role.team.StudyRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRoleRepository extends JpaRepository<StudyRole, Long> {

    Optional<StudyRole> findByTeamIdAndRollName(Long teamId, String rollName);
    List<StudyRole> findAllByTeamIdAndRollName(Long teamId, String rollName);
    List<StudyRole> findAllByTeamId(Long teamId);
    Optional<StudyRole> findByTeamIdAndRollNameAndMemberId(Long teamId, String rollName, Long memberId);

}
