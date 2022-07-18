package com.bibimbap.bibimweb.study.controller;

import com.bibimbap.bibimweb.study.domain.StudyWeek;
import com.bibimbap.bibimweb.study.repository.StudyWeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/study/week")
@RequiredArgsConstructor
public class StudyWeekController {

    private final StudyWeekRepository studyWeekRepository;

    @GetMapping("/")
    public List<StudyWeek> getWeekList(Pageable pageable) {
        return studyWeekRepository.findAll(pageable).getContent();
    }

    @GetMapping("/{id}")
    public StudyWeek getWeekById(@PathVariable Long id) {
        return studyWeekRepository.findById(id).get();
    }

    @PostMapping("/")
    public StudyWeek createWeek(@RequestBody StudyWeek studyWeek) {
        return studyWeekRepository.save(studyWeek);
    }

    @PutMapping("/")
    public StudyWeek updateWeek(@RequestBody StudyWeek studyWeek) {
        return studyWeekRepository.save(studyWeek);
    }

    @DeleteMapping("/{id}")
    public void deleteWeek(@PathVariable Long id) {
        studyWeekRepository.deleteById(id);
    }
}
