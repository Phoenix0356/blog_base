package com.phoenix.blog.model.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class CommentDTO {
    String commentId;
    String commentContent;
    int commentUpvoteCount;
    Timestamp commentPublishTime;
    String commentArticleId;
}
