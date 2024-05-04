package com.phoenix.blog.exceptions;

public class CollectionContainsException extends BaseException{
    public CollectionContainsException() {
        super("Collection Already contains article");
    }

    public CollectionContainsException(String msg) {
        super(msg);
    }
}
