package com.bibimbap.bibimweb.project.controller;

import com.bibimbap.bibimweb.project.domain.ProjectGroup;
import com.bibimbap.bibimweb.project.repository.ProjectGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project/group")
@RequiredArgsConstructor
public class ProjectGroupController {

    private final ProjectGroupRepository projectGroupRepository;

    @GetMapping("/")
    public List<ProjectGroup> getListGroup() {
        return projectGroupRepository.findAll();
    }

    @GetMapping("/{id}")
    public ProjectGroup getGroupById(@PathVariable Long id) {
        return projectGroupRepository.findById(id).get();
    }

    @PostMapping("/")
    public ProjectGroup createGroup(@RequestBody ProjectGroup projectGroup) {
        return projectGroupRepository.save(projectGroup);
    }

    @PutMapping("/")
    public ProjectGroup updateGroup(@RequestBody ProjectGroup projectGroup) {
        return projectGroupRepository.save(projectGroup);
    }

    @DeleteMapping("/{id}")
    public void deleteGroupById(@PathVariable Long id) {
        projectGroupRepository.deleteById(id);
    }
}
