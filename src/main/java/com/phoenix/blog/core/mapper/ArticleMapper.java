package com.phoenix.blog.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.vo.ArticleVO;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    ArticleVO selectArticleWithPublisher(String articleId);
    List<ArticleVO> selectArticleWithPublisherList();
    List<ArticleVO> selectUserArticleList(String userId);
}
