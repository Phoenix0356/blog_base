package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class CommentVO {
    String commentId;
    String commentContent;
    Timestamp commentPublishTime;
    int commentUpvoteCount;

    public static CommentVO buildVO(Comment comment){
        return CommentVO.builder()
                .commentId(comment.getCommentId())
                .commentContent(comment.getCommentContent())
                .commentUpvoteCount(comment.getCommentUpvoteCount())
                .commentPublishTime(comment.getCommentReviseTime())
                .build();
    }
}
