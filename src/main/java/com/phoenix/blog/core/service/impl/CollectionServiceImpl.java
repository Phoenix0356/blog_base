package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.core.mapper.CollectionMapper;
import com.phoenix.blog.core.service.CollectionService;
import com.phoenix.blog.exceptions.CollectionExistException;
import com.phoenix.blog.exceptions.CommentNotFoundException;
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
    public List<ArticleVO> getAllArticleList(String username, String collectionName) {
        return collectionMapper.selectCollectionArticleList(username,collectionName);
    }

    @Override
    public CollectionVO getCollection(String collectionId) {
        Collection collection = collectionMapper.selectById(collectionId);
        return CollectionVO.buildVo(collection);
    }

    @Override
    public List<CollectionVO> getAllCollections(String username) {
        List<Collection> collectionList = collectionMapper.selectList(new QueryWrapper<Collection>()
                .eq("collection_username",username));
        List<CollectionVO> collectionVOList = new ArrayList<>();
        for (Collection collection : collectionList) {
            collectionVOList.add(CollectionVO.buildVo(collection));
        }
        return collectionVOList;
    }

    @Override
    public void saveCollection(CollectionDTO collectionDTO) {
        Collection collection = new Collection();

        String username = collectionDTO.getCollectionUsername();
        String collectionName = collectionDTO.getCollectionName();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("collection_username",username);
        queryMap.put("collection_name",collectionName);

        if (collectionMapper.selectByMap(queryMap)!=null){
            throw new CollectionExistException();
        }
        DataUtil.setFields(collection,collectionDTO,() ->
            collection.setUsername(username)
                    .setCollectionName(collectionName)
                    .setCollectionReviseTime(new Timestamp(System.currentTimeMillis()))

        );
        collectionMapper.insert(collection);
    }

    @Override
    public void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO) {
        Collection collection = collectionMapper.selectOne(new QueryWrapper<Collection>()
                .eq("collection_username",collectionAddDTO.getUsername()));
        Map<String,String> map = new HashMap<>();

        map.put("collectionId", collection.getCollectionId());
        map.put("articleId", collectionAddDTO.getArticleId());
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
