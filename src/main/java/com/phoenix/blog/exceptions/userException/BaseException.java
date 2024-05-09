package com.phoenix.blog.exceptions.userException;

public class BaseException extends RuntimeException{

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
