package com.bibimbap.bibimweb.controller.team;

import com.bibimbap.bibimweb.dto.team.study.StudyTeamCreateDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamResponseDto;
import com.bibimbap.bibimweb.dto.team.study.StudyTeamUpdateDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailCreateDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailResponseDto;
import com.bibimbap.bibimweb.dto.team.study.detail.StudyDetailUpdateDto;
import com.bibimbap.bibimweb.service.team.StudyTeamService;
import com.bibimbap.bibimweb.util.exception.NotFoundException;
import com.bibimbap.bibimweb.util.exception.OutOfRangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/teams/study", produces = "application/json; charset=UTF8")
public class StudyTeamController {

    private final StudyTeamService studyTeamService;

    @PostMapping("/")
    public StudyTeamResponseDto createStudyTeam(@RequestBody StudyTeamCreateDto team) {
        return studyTeamService.createStudyTeam(team);
    }

    @PostMapping("/details/")
    public StudyDetailResponseDto createStudyDetail(@RequestBody StudyDetailCreateDto detail) {
        if (studyTeamService.isNotExistTeam(detail.getTeamId())) {
            throw NotFoundException.STUDY_GROUP;
        }
        return studyTeamService.addStudyDetail(detail);
    }

    @GetMapping("/{teamId}")
    public StudyTeamResponseDto getStudyTeamById(@PathVariable Long teamId) {
        if (studyTeamService.isNotExistTeam(teamId)) {
            throw NotFoundException.STUDY_GROUP;
        }
        return studyTeamService.getStudyTeamById(teamId);
    }

    @GetMapping("/")
    public List<StudyTeamResponseDto> getStudyTeamList(Pageable pageable,
                                                       @RequestParam(required = false, defaultValue = "") String year,
                                                       @RequestParam(required = false, defaultValue = "") String tag) {
        if (!studyTeamService.isValidPage(pageable)) {
            throw OutOfRangeException.PAGE;
        }
        return studyTeamService.getStudyTeamList(pageable, year, tag);
    }

    @GetMapping("/details/{detailId}")
    public StudyDetailResponseDto getStudyDetailById(@PathVariable Long detailId) {
        return studyTeamService.getStudyDetailById(detailId);
    }

    @PutMapping("/")
    public StudyTeamResponseDto updateStudyTeam(@RequestBody StudyTeamUpdateDto team) {

        if (studyTeamService.isNotExistTeam(team.getId())) {
            throw NotFoundException.STUDY_GROUP;
        }
        return studyTeamService.updateStudyTeam(team);
    }

    @PutMapping("/details/")
    public StudyDetailResponseDto updateStudyDetail(@RequestBody StudyDetailUpdateDto detail) {
        return studyTeamService.updateStudyDetail(detail);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity deleteStudyTeam(@PathVariable Long teamId) {
        studyTeamService.deleteStudyTeam(teamId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/details/{detailId}")
    public ResponseEntity deleteStudyDetail(@PathVariable Long detailId) {
        studyTeamService.deleteStudyDetail(detailId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
