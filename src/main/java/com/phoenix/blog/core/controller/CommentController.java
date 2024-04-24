package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.CommentService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.CommentDTO;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.CommentVO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/get")
    public ResultVO getCommentById(@RequestParam String commentId){
        CommentVO commentVO = commentService.getCommentById(commentId);
        return ResultVO.success("Comment load success", commentVO);
    }

    @GetMapping("/all")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getArticleCommentAll(@RequestParam String articleId) throws InterruptedException {
        List<CommentVO> commentVOList = commentService.getCommentArticleList(articleId);
        return ResultVO.success("Comments load success",commentVOList);
    }

    @PostMapping("/save")
    public ResultVO  saveCommentById(@RequestBody CommentDTO commentDTO) throws InterruptedException {
        commentDTO.setCommentUserId(TokenContext.getUserId());
        CommentVO commentVO = commentService.saveComment(commentDTO);
        return ResultVO.success("Comment publish success", commentVO);
    }

    @PutMapping("/update")
    public ResultVO updateCommentById(@RequestBody CommentDTO commentDTO){
        CommentVO commentVO = commentService.updateComment(commentDTO);
        return ResultVO.success("Comment update success",commentVO);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResultVO deleteComment(@PathVariable String commentId){
        commentService.deleteComment(commentId);
        return ResultVO.success("comment delete success",null);
    }
}
