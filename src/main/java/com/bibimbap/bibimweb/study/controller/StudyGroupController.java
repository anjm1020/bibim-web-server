package com.bibimbap.bibimweb.study.controller;

import com.bibimbap.bibimweb.study.domain.StudyGroup;
import com.bibimbap.bibimweb.study.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/study/group")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupRepository studyGroupRepository;

    @GetMapping("/")
    public List<StudyGroup> getGroupList() {
        return studyGroupRepository.findAll();
    }

    @GetMapping("/{id}")
    public StudyGroup getGroupById(@PathVariable Long id) {
        return studyGroupRepository.findById(id).get();
    }

    @PostMapping("/")
    public StudyGroup createList(@RequestBody StudyGroup studyGroup) {

        return studyGroupRepository.save(studyGroup);
    }

    @PutMapping("/")
    public StudyGroup updateGroup(@RequestBody StudyGroup studyGroup) {
        return studyGroupRepository.save(studyGroup);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        studyGroupRepository.deleteById(id);
    }
}
