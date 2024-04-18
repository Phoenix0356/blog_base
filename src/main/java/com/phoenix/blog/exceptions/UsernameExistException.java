package com.phoenix.blog.exceptions;

import java.sql.SQLException;

public class UsernameExistException extends BaseException {
    public UsernameExistException(){
        super("User already exist");
    }
    public UsernameExistException(String msg){
        super(msg);
    }
}
