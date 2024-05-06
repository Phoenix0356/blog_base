package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.core.mapper.CollectionMapper;
import com.phoenix.blog.core.service.CollectionService;
import com.phoenix.blog.exceptions.CollectionContainsException;
import com.phoenix.blog.exceptions.CollectionExistException;
import com.phoenix.blog.model.dto.CollectionAddDTO;
import com.phoenix.blog.model.dto.CollectionDTO;
import com.phoenix.blog.model.entity.Collection;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.CollectionVO;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionMapper collectionMapper;

    @Override
    public List<ArticleVO> getAllArticleList(String collectionId) {
        return collectionMapper.selectCollectionArticleList(collectionId);
    }

    @Override
    public CollectionVO getCollection(String collectionId) {
        Collection collection = collectionMapper.selectById(collectionId);
        return CollectionVO.buildVo(collection);
    }

    @Override
    public List<CollectionVO> getAllCollections(String username) {
        return collectionMapper.selectCollectionsByUsername(username);
    }

    @Override
    public void saveCollection(CollectionDTO collectionDTO, String userId) {
        Collection collection = new Collection();

        String collectionName = collectionDTO.getCollectionName();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("collection_user_id",userId);
        queryMap.put("collection_name",collectionName);

        if (!collectionMapper.selectByMap(queryMap).isEmpty()){
            throw new CollectionExistException();
        }
        DataUtil.setFields(collection,collectionDTO,() ->
            collection.setCollectionUserId(userId)
                    .setCollectionName(collectionName)
                    .setCollectionReviseTime(new Timestamp(System.currentTimeMillis()))

        );
        collectionMapper.insert(collection);
    }

    @Override
    public void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO, String userId) {

        Collection collection = collectionMapper.selectOne(new QueryWrapper<Collection>()
                .eq("collection_user_id",userId)
                .eq("collection_name",collectionAddDTO.getCollectionName())
        );

        String collectionId = collection.getCollectionId();
        String articleId = collectionAddDTO.getArticleId();

        if (collectionMapper.isArticleExistsInCollection(collectionId,articleId) == 1){
            throw new CollectionContainsException();
        }

        Map<String,String> map = new HashMap<>();
        map.put("collectionId",collectionId );
        map.put("articleId", articleId);
        map.put("collectionArticleListId", UUID.randomUUID().toString());
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
