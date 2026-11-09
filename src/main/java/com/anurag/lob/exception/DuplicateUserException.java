package com.anurag.lob.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String msg){
        super(msg);
    }
}
