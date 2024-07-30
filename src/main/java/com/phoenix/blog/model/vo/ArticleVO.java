package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArticleVO {
    String articleId;
    String articleTitle;
    String articleContent;
    String articleReviseTime;
    int articleUpvoteCount;
    int articleReadCount;
    int articleBookmarkCount;

    String username;
    String userAvatarURL;

    String collectionArticleNote;


    public static ArticleVO buildVO(Article article, User user) {
        return new ArticleVO()
                .setArticleId(article.getArticleId())
                .setArticleTitle(article.getArticleTitle())
                .setArticleContent(article.getArticleContent())
                .setArticleReviseTime(article.getArticleReviseTime().toString())
                .setArticleReadCount(article.getArticleReadCount())
                .setArticleUpvoteCount(article.getArticleUpvoteCount())
                .setArticleBookmarkCount(article.getArticleBookmarkCount())
                .setUsername(user.getUsername())
                .setUserAvatarURL(user.getUserAvatarURL());
    }
}
