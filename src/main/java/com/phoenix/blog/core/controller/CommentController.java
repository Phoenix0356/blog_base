package com.phoenix.blog.core.controller;

import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.CommentService;
import com.phoenix.blog.model.dto.CommentDTO;
import com.phoenix.blog.model.entity.Comment;
import com.phoenix.blog.model.vo.CommentVO;
import com.phoenix.blog.model.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/get/{commentId}")
    public ResultVO getCommentById(@PathVariable String commentId){
        Comment comment = commentService.getCommentById(commentId);
        return ResultVO.success("Comment load success", CommentVO.buildVO(comment));
    }

    @PostMapping("/save")
    public ResultVO saveCommentById(@RequestBody CommentDTO commentDTO){
        Comment comment;
        try {
            comment = commentService.saveComment(commentDTO, TokenContext.getUserId());
        }finally {
            TokenContext.removeClaims();
        }


        return ResultVO.success("Comment load success", CommentVO.buildVO(comment));
    }

    @PutMapping("/update")
    public ResultVO updateCommentById(@RequestBody CommentDTO commentDTO){
        Comment comment = commentService.updateComment(commentDTO);

        return ResultVO.success("Comment update success",CommentVO.buildVO(comment));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResultVO deleteComment(@PathVariable String commentId){
        commentService.deleteComment(commentId);
        return ResultVO.success("comment delete success",null);
    }
}
