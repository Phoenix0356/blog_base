package com.phoenix.blog.exceptions.userException;

public class UserNotFoundException extends BaseException{
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String msg) {super(msg);}

}
