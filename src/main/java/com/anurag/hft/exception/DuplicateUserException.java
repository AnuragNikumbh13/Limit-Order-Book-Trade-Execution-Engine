package com.anurag.hft.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String msg){
        super(msg);
    }
}
