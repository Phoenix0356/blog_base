package com.phoenix.blog.exceptions.userException;

public class CollectionNotFoundException extends BaseException{
    public CollectionNotFoundException() {
        super("Collection not found");
    }

    public CollectionNotFoundException(String msg) {
        super(msg);
    }
}
