package com.phoenix.blog.exceptions;

import com.fasterxml.jackson.databind.ser.Serializers;

public class ArticleNotFoundException extends BaseException {
    public ArticleNotFoundException() {
        super("Article not found");
    }

    public ArticleNotFoundException(String msg) {
        super(msg);
    }
}
