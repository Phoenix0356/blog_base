package com.phoenix.blog.exceptions.clientException;

import com.phoenix.blog.constant.RespMessageConstant;

public class ArticleNotOwnerException extends BaseException{
    public ArticleNotOwnerException() {
        super(RespMessageConstant.ARTICLE_NOT_OWNER_ERROR);
    }

    public ArticleNotOwnerException(String msg) {
        super(msg);
    }
}
