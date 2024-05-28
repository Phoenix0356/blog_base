package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.ArticleMessage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleMessageVO {
    String messageProducerUsername;
    String messageType;
    String messageRelatedArticleName;
    boolean messageIsPulled;
    String messageGenerateTime;

    public static ArticleMessageVO buildVO(String messageProducerUsername,
                                           String messageRelatedArticleName,
                                           ArticleMessage articleMessage) {
        return ArticleMessageVO.builder()
                .messageProducerUsername(messageProducerUsername)
                .messageRelatedArticleName(messageRelatedArticleName)
                .messageType(articleMessage.getMessageType().name())
                .messageIsPulled(articleMessage.isMessageIsPulled())
                .messageGenerateTime(articleMessage.getMessageGenerateTime().toString())
                .build();
    }
}
