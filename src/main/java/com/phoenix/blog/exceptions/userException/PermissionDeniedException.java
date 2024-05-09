package com.phoenix.blog.exceptions.userException;

public class PermissionDeniedException extends BaseException{
    public PermissionDeniedException() {
        super("Permission denied");
    }

    public PermissionDeniedException(String msg) {
        super(msg);
    }
}
