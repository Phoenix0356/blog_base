package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.core.mapper.MessageMapper;
import com.phoenix.blog.core.mapper.UserMapper;
import com.phoenix.blog.core.service.MessageService;
import com.phoenix.blog.enumeration.MessageType;
import com.phoenix.blog.model.dto.ArticleMessageDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.ArticleMessage;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.model.vo.ArticleMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    final MessageMapper messageMapper;
    final ArticleMapper articleMapper;
    final UserMapper userMapper;

    @Override
    public ArticleMessageVO getMessageByMessageId(String messageId) {
        ArticleMessage articleMessage = messageMapper.selectById(messageId);
        Article article = articleMapper.selectById(articleMessage.getMessageId());
        User user = userMapper.selectById(articleMessage.getMessageProducerId());
        return ArticleMessageVO.buildVO(user.getUsername(),article.getArticleTitle(), articleMessage);
    }

    @Override
    public List<ArticleMessageVO> getMessageListByReceiverId(String receiverId) {
        return messageMapper.getMessageList(receiverId);
    }

    @Override
    @Async("asyncServiceExecutor")
    public void saveMessage(String messageRelatedArticleId,MessageType messageType, String producerId) {
        Article article = articleMapper.selectById(messageRelatedArticleId);
        User user = userMapper.selectById(article.getArticleUserId());

        ArticleMessage articleMessage = messageMapper.selectOne(new QueryWrapper<ArticleMessage>()
                .eq("message_producer_id",producerId)
                .eq("message_receiver_id",article.getArticleUserId())
                .eq("message_type",messageType)
                .orderByDesc("message_generate_time")
                .last("Limit 1")
        );

        if (articleMessage == null||articleMessage.isMessageIsPulled()) {
            articleMessage = new ArticleMessage();
            articleMessage.setMessageProducerId(producerId)
                    .setMessageReceiverId(user.getUserId())
                    .setMessageRelatedArticleId(messageRelatedArticleId)
                    .setMessageType(messageType)
                    .setMessageIsPulled(false)
                    .setMessageGenerateTime(new Timestamp(System.currentTimeMillis()));
            messageMapper.insert(articleMessage);
        }else {
            articleMessage.setMessageGenerateTime(new Timestamp(System.currentTimeMillis()));
            messageMapper.updateById(articleMessage);
        }


    }


}
