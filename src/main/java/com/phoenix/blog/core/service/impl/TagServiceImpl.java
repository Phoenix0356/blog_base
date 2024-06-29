package com.phoenix.blog.core.service.impl;

import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.manager.ArticleManager;
import com.phoenix.blog.core.mapper.TagMapper;
import com.phoenix.blog.core.service.ArticleService;
import com.phoenix.blog.core.service.TagService;
import com.phoenix.blog.exceptions.clientException.ArticleNotOwnerException;
import com.phoenix.blog.model.dto.ArticleAddTagDTO;
import com.phoenix.blog.model.dto.TagDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.Tag;
import com.phoenix.blog.model.vo.TagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    final private TagMapper tagMapper;
    final private ArticleManager articleManager;

    @Override
    public List<TagVO> getTagList() {
        return tagMapper.selectList(null).stream()
                .map(TagVO::buildVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagVO> getArticleTagsList(String articleId) {
        return tagMapper.getArticleTagList(articleId);
    }

    @Override
    public void saveTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setTagContent(tagDTO.getTagContent())
                .setTagReviseTime(new Timestamp(System.currentTimeMillis()))
                .setTagCreateUserId(TokenContext.getUserId());
        tagMapper.insert(tag);
    }

    @Override
    public void addTagToArticle(ArticleAddTagDTO articleAddTagDTO) {

       Article article = articleManager.getArticleById(articleAddTagDTO.getArticleId());
       if (!article.getArticleUserId().equals(TokenContext.getUserId())){
           throw new ArticleNotOwnerException();
       }

        for(String tagId: articleAddTagDTO.getTagIdList()) {
            Map<String, String> map = new HashMap<>();
            map.put("articleTagListId", UUID.randomUUID().toString());
            map.put("articleId", articleAddTagDTO.getArticleId());
            map.put("tagId", tagId);
            tagMapper.addTagToArticle(map);
        }
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        Tag tag = tagMapper.selectById(tagDTO.getTagId());
        tag.setTagReviseTime(new Timestamp(System.currentTimeMillis()))
                .setTagContent(tagDTO.getTagContent());
        tagMapper.updateById(tag);
    }

    @Override
    public void deleteTagById(String tagId) {
        tagMapper.deleteById(tagId);
    }
}
