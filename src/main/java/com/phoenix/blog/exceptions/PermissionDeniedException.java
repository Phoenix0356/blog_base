package com.phoenix.blog.exceptions;

public class PermissionDeniedException extends BaseException{
    public PermissionDeniedException() {
        super("Permission denied");
    }

    public PermissionDeniedException(String msg) {
        super(msg);
    }
}
