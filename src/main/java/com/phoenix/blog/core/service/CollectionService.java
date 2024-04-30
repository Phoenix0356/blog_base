package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.CollectionAddDTO;
import com.phoenix.blog.model.dto.CollectionDTO;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.CollectionVO;

import java.util.Collection;
import java.util.List;

public interface CollectionService {
    public CollectionVO getCollection(String collectionId);
    public List<CollectionVO> getAllCollections(String username);
    public List<ArticleVO> getAllArticleList(String username, String collectionName);

    public void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO);

    public void saveCollection(CollectionDTO collectionDTO);

    public void updateArticleFromCollection();

    public void deleteArticleFromCollection(String articleId);

}
