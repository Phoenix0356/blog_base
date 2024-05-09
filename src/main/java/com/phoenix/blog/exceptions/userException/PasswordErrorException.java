package com.phoenix.blog.exceptions.userException;

public class PasswordErrorException extends BaseException {

    public PasswordErrorException() {
        super("wrong password");
    }

    public PasswordErrorException(String msg) {
        super(msg);
    }

}