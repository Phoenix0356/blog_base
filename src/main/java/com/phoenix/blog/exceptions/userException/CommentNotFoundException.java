package com.phoenix.blog.exceptions.userException;

public class CommentNotFoundException extends BaseException {
    public CommentNotFoundException() {
        super("Comment not found");
    }

    public CommentNotFoundException(String msg) {
        super(msg);
    }
}