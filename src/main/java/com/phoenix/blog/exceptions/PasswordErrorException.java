package com.phoenix.blog.exceptions;

public class PasswordErrorException extends BaseException {

    public PasswordErrorException() {
        super("wrong password");
    }

    public PasswordErrorException(String msg) {
        super(msg);
    }

}