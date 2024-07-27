package com.phoenix.blog.core.manager;

import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.model.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleManager {
    private final ArticleMapper articleMapper;

    public Article selectArticleById(String articleId){
        return articleMapper.selectById(articleId);
    }
}
