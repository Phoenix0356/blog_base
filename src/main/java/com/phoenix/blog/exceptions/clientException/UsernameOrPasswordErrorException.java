package com.phoenix.blog.exceptions.clientException;

public class UsernameOrPasswordErrorException extends BaseException{
    public UsernameOrPasswordErrorException() {
        super("Username or password error");
    }

    public UsernameOrPasswordErrorException(String msg) {super(msg);}

}
