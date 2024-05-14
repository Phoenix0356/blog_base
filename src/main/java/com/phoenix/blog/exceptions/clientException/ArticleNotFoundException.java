package com.phoenix.blog.exceptions.clientException;

public class ArticleNotFoundException extends BaseException {
    public ArticleNotFoundException() {
        super("Article not found");
    }

    public ArticleNotFoundException(String msg) {
        super(msg);
    }
}
