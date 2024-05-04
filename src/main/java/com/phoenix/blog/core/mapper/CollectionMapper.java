package com.phoenix.blog.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.model.entity.Collection;
import com.phoenix.blog.model.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
    List<ArticleVO> selectCollectionArticleList(String username, String collectionName);

    void insertArticleIntoCollection(Map<String,String> map);

    void deleteArticleFromCollection(String articleId);

    int isArticleExistsInCollection(String collectionId,String articleId);
}
