package com.bibimbap.bibimweb.aop;

import com.bibimbap.bibimweb.util.dto.ErrorResultDto;
import com.bibimbap.bibimweb.util.exception.ConflictException;
import com.bibimbap.bibimweb.util.exception.NotFoundException;
import com.bibimbap.bibimweb.util.exception.OutOfRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity handleMemberConflictException(ConflictException e) {
        return new ResponseEntity(new ErrorResultDto(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OutOfRangeException.class)
    public ResponseEntity handleOutOfPageNumberException(OutOfRangeException e) {
        return new ResponseEntity(new ErrorResultDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e) {
        return new ResponseEntity(new ErrorResultDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
