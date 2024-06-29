package com.phoenix.blog.exceptions.clientException;

import com.phoenix.blog.constant.RespMessageConstant;

public class ArticleNotFoundException extends BaseException {
    public ArticleNotFoundException() {
        super(RespMessageConstant.ARTICLE_NOT_FOUND_ERROR);
    }

    public ArticleNotFoundException(String msg) {
        super(msg);
    }
}
