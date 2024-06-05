package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.ArticleService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.core.service.UserService;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        ArticleVO articleVO = articleService.getArticleById(articleId);
        return ResultVO.success("Article load success", articleVO);
    }

    @GetMapping("/get/all")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getArticleAll(@RequestParam("sortBy") int sortStrategy){
        List<ArticleVO> articleVOList;
        try {
            articleVOList = articleService.getArticleAll(sortStrategy);
        }finally {
            TokenContext.removeClaims();
        }
        return ResultVO.success("Articles load success",articleVOList);
    }
    @GetMapping("/list")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO getUserArticleListById(){
        List<ArticleVO> articleVOList;
        try {
            articleVOList = articleService.getArticleUserList(TokenContext.getUserId());
        }finally {
            TokenContext.removeClaims();
        }

        return ResultVO.success("User Articles load success",articleVOList);
    }

    @PostMapping("/save")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO saveArticle(@RequestBody ArticleDTO articleDTO){
        try {
            articleDTO.setArticleUserId(TokenContext.getUserId());
            articleService.SaveArticleByUser(articleDTO);
        }finally {
            TokenContext.removeClaims();
        }

        return ResultVO.success("Article save success");
    }

    //更新文章内容
    @PutMapping("/update/content")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO updateArticleContent(@RequestBody ArticleDTO articleDTO){
        articleService.updateArticleContent(articleDTO);
        return ResultVO.success("Article save success");
    }

    //更新阅读和点赞数
    @PutMapping("/update/statics")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO updateArticleStatics(@RequestBody ArticleDTO articleDTO){

        articleService.updateArticleStatics(articleDTO);
        return ResultVO.success("Article save success");
    }

    @DeleteMapping("/delete/{articleId}")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO deleteArticle(@PathVariable String articleId){
        articleService.deleteArticleById(articleId);
        return ResultVO.success("Article delete success",null);
    }
}
