package com.bibimbap.bibimweb.util.exception;

public class NotFoundException extends RuntimeException {
    public static final NotFoundException ROLE = new NotFoundException("존재하지 않는 역할입니다");
    public static final NotFoundException MEMBER = new NotFoundException("존재하지 않는 회원입니다");
    public static final NotFoundException PROJECT_GROUP = new NotFoundException("존재하지 않는 프로젝트입니다");
    public static final NotFoundException STUDY_GROUP = new NotFoundException("존재하지 않는 스터디입니다");
    private NotFoundException(String message) {
        super(message);
    }
}
