package com.phoenix.blog.exceptions;

public class UserNotFoundException extends BaseException{
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String msg) {super(msg);}

}
