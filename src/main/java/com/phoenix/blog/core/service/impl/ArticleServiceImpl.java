package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.core.service.ArticleService;
import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.exceptions.ArticleFormatException;
import com.phoenix.blog.exceptions.ArticleNotFoundException;
import com.phoenix.blog.exceptions.InvalidateArgumentException;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    final ArticleMapper articleMapper;

    @Override
    @Transactional
    public Article getArticleById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article = articleMapper.selectById(articleId);

        if (article == null){
            throw new ArticleNotFoundException();
        }

        return article;
    }

    @Override
    @Transactional
    public List<Article> getArticleAll() {
        return articleMapper.selectList(null);
    }

    @Override
    @Transactional
    public List<Article> getArticleUSerList(String userId) {
        return articleMapper.selectList(new QueryWrapper<Article>().eq("article_user_id",userId));
    }

    @Override
    @Transactional
    public Article SaveArticleByUser(ArticleDTO articleDTO) {
        Article article = new Article();

        DataUtil.setFields(article,articleDTO,() ->
                article.setArticleUserId(articleDTO.getArticleUserId())
                .setArticleTitle(articleDTO.getArticleTitle())
                .setArticleContent(articleDTO.getArticleContent())
                .setArticleReviseTime(new Timestamp(System.currentTimeMillis())));

        if (DataUtil.isEmptyData(articleDTO.getArticleTitle())){
            throw new ArticleFormatException("title is empty");
        }

        if (DataUtil.isEmptyData(articleDTO.getArticleContent())){
            throw new ArticleFormatException("content is empty");
        }

        articleMapper.insert(article);
        return article;
    }

    @Override
    @Transactional
    public Article updateArticleContent(ArticleDTO articleDTO) {
        String articleId = articleDTO.getArticleId();

        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new ArticleNotFoundException();

        DataUtil.setFields(article,articleDTO,() ->
                article.setArticleTitle(articleDTO.getArticleTitle())
                .setArticleContent(articleDTO.getArticleContent())
                .setArticleReviseTime(new Timestamp(System.currentTimeMillis())));


        articleMapper.update(article,new UpdateWrapper<Article>().eq("article_id",articleId));

        return article;
    }

    @Override
    @Transactional
    public Article updateArticleStatics(ArticleDTO articleDTO) {
        String articleId = articleDTO.getArticleId();

        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new ArticleNotFoundException();

        DataUtil.setFields(article, articleDTO, () ->
               article.setArticleReadCount(articleDTO.getArticleReadCount())
                    .setArticleUpvoteCount(articleDTO.getArticleUpvoteCount()));

        articleMapper.update(article,new UpdateWrapper<Article>().eq("article_id",articleId));

        return article;
    }

    @Override
    @Transactional
    public void deleteArticleById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new ArticleNotFoundException();

        articleMapper.delete(new QueryWrapper<Article>().eq("article_id",articleId));

    }
}
