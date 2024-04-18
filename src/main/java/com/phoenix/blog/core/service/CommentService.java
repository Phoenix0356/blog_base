package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.CommentDTO;
import com.phoenix.blog.model.entity.Comment;

public interface CommentService {
    public Comment getCommentById(String commentId);

    public Comment saveComment(CommentDTO commentDTO, String userId);

    public Comment updateComment(CommentDTO commentDTO);

    public void deleteComment(String commentId);
}
