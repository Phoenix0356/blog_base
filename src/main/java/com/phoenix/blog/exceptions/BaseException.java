package com.phoenix.blog.exceptions;

public class BaseException extends RuntimeException{

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
