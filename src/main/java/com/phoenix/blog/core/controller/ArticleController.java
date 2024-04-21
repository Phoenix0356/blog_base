package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.ArticleService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.core.service.UserService;
import com.phoenix.blog.util.DataUtil;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    final ArticleService articleService;
    final UserService userService;
    @GetMapping("/get/{articleId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getArticleById(@PathVariable String articleId){
        Article article = articleService.getArticleById(articleId);
        User publisher = userService.getUser(article.getArticleUserId());
        return ResultVO.success("Article load success", ArticleVO.buildVO(article,publisher));
    }

    @GetMapping("/get/all")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getArticleAll(){
        List<ArticleVO> articleVOList;
        try {
            List<Article> articleList = articleService.getArticleAll();
            articleVOList = new ArrayList<>();
            for (Article article : articleList) {
                User publisher = userService.getUser(article.getArticleUserId());
                articleVOList.add(ArticleVO.buildVO(article, publisher));
            }
        }finally {
            TokenContext.removeClaims();
        }
        return ResultVO.success("Articles load success",articleVOList);
    }
    @GetMapping("/list")
    @AuthorizationRequired(Role.PUBLISHER)
    public ResultVO getUserArticleListById(){
        List<ArticleVO> articleVOList;
        try {
            List<Article> articleList = articleService.getArticleUSerList(TokenContext.getUserId());
            articleVOList = new ArrayList<>();
            User user = userService.getUser(TokenContext.getUserId());
            for (Article article : articleList) {
                articleVOList.add(ArticleVO.buildVO(article, user));
            }
        }finally {
            TokenContext.removeClaims();
        }

        return ResultVO.success("User Articles load success",articleVOList);
    }

    @PostMapping("/save")
    @AuthorizationRequired(Role.PUBLISHER)
    public ResultVO saveArticle(@RequestBody ArticleDTO articleDTO){
        Article article;
        try {
            articleDTO.setArticleUserId(TokenContext.getUserId());
            article = articleService.SaveArticleByUser(articleDTO);
        }finally {
            TokenContext.removeClaims();
        }

        return ResultVO.success("Article save success",ArticleVO.buildVO(article,null));
    }


    @PutMapping("/update/content")
    @AuthorizationRequired(Role.PUBLISHER)
    //更新文章内容
    public ResultVO updateArticleContent(@RequestBody ArticleDTO articleDTO){
        Article article;

        article = articleService.updateArticleContent(articleDTO);
        return ResultVO.success("Article save success",ArticleVO.buildVO(article,null));
    }

    @PutMapping("/update/statics")
    @AuthorizationRequired(Role.VISITOR)
    //更新阅读和点赞数
    public ResultVO updateArticleStatics(@RequestBody ArticleDTO articleDTO){
        Article article;
        article = articleService.updateArticleStatics(articleDTO);
        return ResultVO.success("Article save success",ArticleVO.buildVO(article,null));
    }
    //Todo

    @PutMapping("/collect")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO collectArticleById(@RequestParam String articleId){
        return null;
    }


    @DeleteMapping("/delete/{articleId}")
    @AuthorizationRequired(Role.PUBLISHER)
    public ResultVO deleteArticle(@PathVariable String articleId){
        articleService.deleteArticleById(articleId);
        return ResultVO.success("Article delete success",null);
    }
}
