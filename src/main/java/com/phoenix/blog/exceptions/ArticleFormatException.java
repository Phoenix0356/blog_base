package com.phoenix.blog.exceptions;

public class ArticleFormatException extends BaseException{
    public ArticleFormatException() {
        super("Article with illegal format");
    }

    public ArticleFormatException(String msg) {
        super(msg);
    }
}
