package com.bibimbap.bibimweb.controller.team;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/teams/study", produces = "application/json; charset=UTF8")
public class StudyTeamController {
}

