package com.phoenix.blog.exceptions.clientException;

public class CollectionExistException extends BaseException {
    public CollectionExistException() {
        super("Collection already exist");
    }

    public CollectionExistException(String msg) {
        super(msg);
    }
}
