package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.core.service.CollectionService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.CollectionAddDTO;
import com.phoenix.blog.model.dto.CollectionDTO;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;
    @GetMapping("/get/{articleId}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getArticleById(@PathVariable String articleId){
        ArticleVO articleVO = collectionService.getArticleById(articleId);
        return ResultVO.success("get article success",articleVO);
    }

    @GetMapping("/get/all")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getAllArticles(@RequestParam String username){
        List<ArticleVO> allArticleList = collectionService.getAllArticleList(username);
        return ResultVO.success("load articles success",allArticleList);
    }

    //添加文章
    @PostMapping("/add")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO saveArticleIntoCollection(@RequestBody CollectionAddDTO collectionAddDTO){
        collectionService.saveArticleIntoCollection(collectionAddDTO);
        return ResultVO.success("add article success");
    }

    //创建收藏夹
    //Todo
    @PostMapping("/save")
    public ResultVO saveCollection(@RequestBody CollectionDTO collectionDTO){

        return ResultVO.success("create collection success");
    }

    //Todo
    @PutMapping("/update")
    public ResultVO updateCollection(@RequestBody String articleId){
        return null;
    }


    @DeleteMapping("/delete")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO deleteArticleFromCollection(@RequestParam String articleId){
        collectionService.deleteArticleFromCollection(articleId);
        return ResultVO.success("article delete success");
    }

}
