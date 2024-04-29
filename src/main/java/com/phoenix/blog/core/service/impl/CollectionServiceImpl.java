package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.core.mapper.CollectionMapper;
import com.phoenix.blog.core.service.CollectionService;
import com.phoenix.blog.model.dto.CollectionAddDTO;
import com.phoenix.blog.model.dto.CollectionDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.Collection;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionMapper collectionMapper;
    private final ArticleMapper articleMapper;


    @Override
    public ArticleVO getArticleById(String articleId) {
        Article article = articleMapper.selectById(articleId);
        return ArticleVO.buildVO(article);
    }

    @Override
    public List<ArticleVO> getAllArticleList(String username) {
        return collectionMapper.selectCollectionArticleList(username);
    }

    @Override
    public void saveCollection(CollectionDTO collectionDTO) {
        Collection collection = new Collection();
        DataUtil.setFields(collection,collectionDTO,() ->
            collection.setUsername(collectionDTO.getCollectionUsername())
                    .setCollectionName(collectionDTO.getCollectionName())
                    .setCollectionReviseTime(new Timestamp(System.currentTimeMillis()))

        );
        collectionMapper.insert(collection);
    }

    @Override
    public void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO) {
        Collection collection = collectionMapper.selectOne(new QueryWrapper<Collection>()
                .eq("collection_username",collectionAddDTO.getUsername()));
        Map<String,String> map = new HashMap<>();
        map.put("collectionArticleListId", UUID.randomUUID().toString());
        map.put("collectionId", collection.getCollectionId());
        map.put("articleId", collectionAddDTO.getArticleId());
        collectionMapper.insertArticleIntoCollection(map);
    }

    @Override
    public void updateArticleFromCollection() {

    }

    @Override
    public void deleteArticleFromCollection(String articleId) {
        collectionMapper.deleteArticleFromCollection(articleId);
    }
}
