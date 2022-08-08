package com.bibimbap.bibimweb.repository.team.tag;

import com.bibimbap.bibimweb.domain.team.tag.TeamTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamTagRepository extends JpaRepository<TeamTag, Long> {
    List<TeamTag> findAllByTeamId(Long teamId);

    void deleteByTagId(Long tagId);
    boolean existsByTagId(Long tagId);

    Optional<TeamTag> findByTagIdAndTeamId(Long tagId, Long teamId);
}
