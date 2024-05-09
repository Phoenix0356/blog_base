package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.vo.ArticleVO;

import java.util.List;


public interface ArticleService {

    public ArticleVO getArticleById(String articleId);
    public List<ArticleVO> getArticleAll(int sortStrategy);

    public List<ArticleVO> getArticleUSerList(String userId);
    public void SaveArticleByUser(ArticleDTO articleDTO);
    public void updateArticleContent(ArticleDTO articleDTO);

    public void updateArticleStatics(ArticleDTO articleDTO);
    public void deleteArticleById(String articleId);
}
