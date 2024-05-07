package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.CollectionAddDTO;
import com.phoenix.blog.model.dto.CollectionDTO;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.CollectionVO;

import java.util.List;

public interface CollectionService {
    public CollectionVO getCollection(String collectionId);
    public List<CollectionVO> getAllCollections(String username);
    public List<ArticleVO> getAllArticleList(String collectionId);

    public void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO, String userId);

    public void saveCollection(CollectionDTO collectionDTO, String userId);

    public void updateCollection(CollectionDTO collectionDTO);

    public void deleteArticleFromCollection(String articleId);

    public void deleteCollectionById(String collectionId);

}
