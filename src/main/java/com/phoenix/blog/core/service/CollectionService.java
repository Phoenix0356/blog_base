package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.CollectionAddDTO;
import com.phoenix.blog.model.dto.CollectionDTO;
import com.phoenix.blog.model.vo.ArticleVO;

import java.util.List;

public interface CollectionService {
    public ArticleVO getArticleById(String articleId);
    public List<ArticleVO> getAllArticleList(String username);

    public void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO);

    public void saveCollection(CollectionDTO collectionDTO);

    public void updateArticleFromCollection();

    public void deleteArticleFromCollection(String articleId);

}
