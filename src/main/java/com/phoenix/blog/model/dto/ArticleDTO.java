package com.phoenix.blog.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class ArticleDTO {
    String articleId;
    String articleUserId;

    String articleTitle;
    String articleContent;
    String articlePublishTime;

    int articleReadCount;
    int articleUpvoteCount;
    int articleBookmarkCount;
}
