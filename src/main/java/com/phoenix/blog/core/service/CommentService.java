package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.CommentDTO;
import com.phoenix.blog.model.vo.CommentVO;

import java.util.List;

public interface CommentService {
    public CommentVO getCommentById(String commentId);

    public List<CommentVO> getCommentArticleList(String articleId) throws InterruptedException;

    public CommentVO saveComment(CommentDTO commentDTO) throws InterruptedException;

    public CommentVO updateComment(CommentDTO commentDTO);

    public void deleteComment(String commentId);
}
