package com.bibimbap.bibimweb.project.controller;

import com.bibimbap.bibimweb.project.dto.ProjectGroupCreateDto;
import com.bibimbap.bibimweb.project.dto.ProjectGroupResponseDto;
import com.bibimbap.bibimweb.project.dto.ProjectGroupUpdateDto;
import com.bibimbap.bibimweb.project.repository.ProjectGroupRepository;
import com.bibimbap.bibimweb.project.service.ProjectGroupService;
import com.bibimbap.bibimweb.util.exception.NotFoundException;
import com.bibimbap.bibimweb.util.exception.OutOfRangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/project/groups", produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
public class ProjectGroupController {

    private final ProjectGroupRepository projectGroupRepository;
    private final ProjectGroupService projectGroupService;
    @PostMapping("/")
    public ProjectGroupResponseDto createGroup(@RequestBody ProjectGroupCreateDto projectGroup) {
        return projectGroupService.createProjectGroup(projectGroup);
    }

    @GetMapping("/")
    public List<ProjectGroupResponseDto> getGroupList(Pageable pageable) {
        if (!projectGroupService.isValidPage(pageable)) {
            throw OutOfRangeException.PAGE;
        }
        return projectGroupService.getProjectGroupList(pageable);
    }

    @GetMapping(value = "/", params = "period")
    public List<ProjectGroupResponseDto> getGroupByPeriod(@RequestParam String period) {
        return projectGroupService.getProjectGroupByPeriod(period);
    }

    @GetMapping("/{id}")
    public ProjectGroupResponseDto getGroupById(@PathVariable Long id) {
        if (!projectGroupService.isExistGroup(id)) {
            throw NotFoundException.PROJECT_GROUP;
        }
        return projectGroupService.getProjectGroupById(id);
    }

    @PutMapping("/")
    public ProjectGroupResponseDto updateGroup(@RequestBody ProjectGroupUpdateDto projectGroup) {
        if (!projectGroupService.isExistGroup(projectGroup.getId())) {
            throw NotFoundException.PROJECT_GROUP;
        }
        return projectGroupService.updateProjectGroup(projectGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGroupById(@PathVariable Long id) {
        if (!projectGroupService.isExistGroup(id)) {
            throw NotFoundException.PROJECT_GROUP;
        }
        projectGroupService.deleteProjectGroup(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
