package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.phoenix.blog.core.mapper.CommentMapper;
import com.phoenix.blog.core.service.CommentService;
import com.phoenix.blog.model.dto.CommentDTO;
import com.phoenix.blog.model.entity.Comment;
import com.phoenix.blog.exceptions.CommentFormatException;
import com.phoenix.blog.exceptions.CommentNotFoundException;
import com.phoenix.blog.exceptions.InvalidateArgumentException;
import com.phoenix.blog.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Comment getCommentById(String commentId) {
        if (DataUtil.isEmptyData(commentId)){
            throw  new InvalidateArgumentException();
        }

        Comment comment = commentMapper.selectById(commentId);

        if (comment == null){
            throw new CommentNotFoundException();
        }

        return comment;
    }

    @Override
    public Comment saveComment(CommentDTO commentDTO, String userId) {
        if (commentDTO.getCommentContent() == null){
            throw new CommentFormatException();
        }
        Comment comment = new Comment();
        comment.set(commentDTO,userId);
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public Comment updateComment(CommentDTO commentDTO) {
        String commentId = commentDTO.getCommentId();
        if (DataUtil.isEmptyData(commentId)) throw new InvalidateArgumentException();

        Comment comment = commentMapper.selectById(commentId);

        if (comment == null) throw new CommentNotFoundException();
        comment.update(commentDTO);

        commentMapper.update(comment,new UpdateWrapper<Comment>().eq("comment_id",commentId));

        return comment;
    }

    @Override
    public void deleteComment(String commentId) {
        if (DataUtil.isEmptyData(commentId)) throw new InvalidateArgumentException();

        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) throw new CommentNotFoundException();

        commentMapper.delete(new QueryWrapper<Comment>().eq("comment_id",commentId));

    }
}
