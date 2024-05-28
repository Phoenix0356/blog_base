package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.ArticleMessageDTO;
import com.phoenix.blog.model.vo.ArticleMessageVO;

import java.util.List;

public interface MessageService {
    ArticleMessageVO getMessageByMessageId(String messageId);
    List<ArticleMessageVO> getMessageListByReceiverId(String receiverId);

    void saveMessage(ArticleMessageDTO articleMessageDTO, String producerId);
}
