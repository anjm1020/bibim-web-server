package com.bibimbap.bibimweb.util.exception;

public class ConflictException extends RuntimeException {
    public static final ConflictException MEMBER = new ConflictException("중복 회원입니다");
    private ConflictException(String message) {
        super(message);
    }

}
