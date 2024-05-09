package com.phoenix.blog.exceptions.userException;

public class InvalidateArgumentException extends BaseException{
    public InvalidateArgumentException() {
        super("Invalidate value");
    }

    public InvalidateArgumentException(String msg) {
        super(msg);
    }
}
