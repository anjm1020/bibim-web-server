package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.team.Team;
import com.bibimbap.bibimweb.domain.team.tag.Tag;
import com.bibimbap.bibimweb.domain.team.tag.TeamTag;
import com.bibimbap.bibimweb.repository.team.TeamRepository;
import com.bibimbap.bibimweb.repository.team.tag.TagRepository;
import com.bibimbap.bibimweb.repository.team.tag.TeamTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TeamRepository teamRepository;
    private final TagRepository tagRepository;
    private final TeamTagRepository teamTagRepository;

    // tag create
    public List<TeamTag> saveTags(Long teamId, List<String> tagNames) {

        return tagNames.stream()
                .map(name -> {
                    Optional<Tag> tagOptional = tagRepository.findByName(name);
                    if (!tagOptional.isEmpty()) {
                        return tagOptional.get();
                    }
                    return tagRepository.save(Tag.builder()
                            .name(name)
                            .build());
                })
                .map(saved ->
                        {
                            Optional<TeamTag> optionalTeamTag = teamTagRepository.findByTagIdAndTeamId(saved.getId(), teamId);
                            if (!optionalTeamTag.isEmpty()) {
                                return optionalTeamTag.get();
                            }

                            Team team = teamRepository.findById(teamId).get();
                            TeamTag teamTag = new TeamTag();
                            teamTag.setTeam(team);
                            teamTag.setTag(saved);
                            TeamTag savedTeamTag = teamTagRepository.save(teamTag);
                            return savedTeamTag;
                        }
                )
                .collect(Collectors.toList());
    }

    public void updateTags(Long teamId, List<String> newTagNames) {

        Team team = teamRepository.findById(teamId).get();
        List<TeamTag> deleteList = new ArrayList<>();
        team.getTags().forEach(tt -> {
            if (!newTagNames.contains(tt.getTag().getName())) {
                deleteList.add(tt);
            }
        });
        deleteList.forEach(tt -> {
            this.deleteTeamTagById(tt.getId());
            team.getTags().remove(tt);
        });
        this.saveTags(teamId, newTagNames);
    }

    // teamTag delete
    public void deleteAllTeamTag(Long teamId) {
        List<TeamTag> teamTags = teamTagRepository.findAllByTeamId(teamId);
        for (TeamTag teamTag : teamTags) {
            this.deleteTeamTagById(teamTag.getId());
        }
    }

    public void deleteTeamTagById(Long teamTagId) {
        Long tagId = teamTagRepository.findById(teamTagId).get().getTag().getId();
        teamTagRepository.deleteById(teamTagId);
        if (!teamTagRepository.existsByTagId(tagId)) {
            tagRepository.deleteById(tagId);
        }
    }
}
