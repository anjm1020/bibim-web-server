package com.bibimbap.bibimweb.service.team;

import com.bibimbap.bibimweb.domain.team.ProjectTeam;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.project.ProjectTeamResponseDto;
import com.bibimbap.bibimweb.repository.team.ProjectTeamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectTeamService {

    private ProjectTeamRepository projectTeamRepository;

    private final ModelMapper mapper = new ModelMapper();

    public boolean isValidPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long count = projectTeamRepository.count();
        return 0 <= pageNumber && pageNumber <= ((count - 1) / pageSize);
    }

    public boolean isExistGroup(Long groupId) {
        return projectTeamRepository.existsById(groupId);
    }

    public ProjectTeamResponseDto createProjectTeam(ProjectTeamCreateDto dto) {
        ProjectTeam newTeam = mapper.map(dto, ProjectTeam.class);
        return null;
    }

}
