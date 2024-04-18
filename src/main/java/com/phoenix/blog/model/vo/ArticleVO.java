package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.User;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleVO {
    String articleId;
    String articleTitle;
    String articleContent;
    String articleReviseTime;
    int articleUpvoteCount;
    int articleReadCount;
    int articleBookmarkCount;

    String username;
    String avatarURL;

    public static ArticleVO buildVO(Article article, @Nullable User user) {
        return ArticleVO.builder()
                .articleId(article.getArticleId())
                .articleTitle(article.getArticleTitle())
                .articleContent(article.getArticleContent())
                .articleReviseTime(article.getArticleReviseTime().toString())
                .articleReadCount(article.getArticleReadCount())
                .articleUpvoteCount(article.getArticleUpvoteCount())
                .articleBookmarkCount(article.getArticleBookmarkCount())
                .username(user == null?null:user.getUsername())
                //Todo
                .avatarURL(user == null?null:user.getUserAvatarURL())
                .build();
    }
}
