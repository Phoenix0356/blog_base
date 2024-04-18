package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.entity.Article;

import java.util.List;


public interface ArticleService {

    public Article getArticleById(String articleId);
    public List<Article> getArticleAll();

    public List<Article> getArticleUSerList(String userId);
    public Article SaveArticleByUser(ArticleDTO articleDTO);
    public Article updateArticleContent(ArticleDTO articleDTO);

    public Article updateArticleStatics(ArticleDTO articleDTO);
    public void deleteArticleById(String articleId);
}
