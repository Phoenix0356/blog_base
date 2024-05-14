package com.phoenix.blog.exceptions.clientException;

public class PasswordErrorException extends BaseException {

    public PasswordErrorException() {
        super("wrong password");
    }

    public PasswordErrorException(String msg) {
        super(msg);
    }

}