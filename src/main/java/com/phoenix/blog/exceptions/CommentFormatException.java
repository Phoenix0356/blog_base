package com.phoenix.blog.exceptions;

public class CommentFormatException extends BaseException{
    public CommentFormatException() {
        super("Comment with illegal format");
    }

    public CommentFormatException(String msg) {
        super(msg);
    }
}
