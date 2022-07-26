package com.bibimbap.bibimweb.util.exception;

public class OutOfRangeException extends RuntimeException {
    public static final OutOfRangeException PAGE = new OutOfRangeException("유효하지 않은 페이지입니다");

    private OutOfRangeException(String message) {
        super(message);
    }
}
