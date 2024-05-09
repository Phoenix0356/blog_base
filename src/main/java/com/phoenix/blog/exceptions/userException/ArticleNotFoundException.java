package com.phoenix.blog.exceptions.userException;

public class ArticleNotFoundException extends BaseException {
    public ArticleNotFoundException() {
        super("Article not found");
    }

    public ArticleNotFoundException(String msg) {
        super(msg);
    }
}
