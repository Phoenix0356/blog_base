package com.phoenix.blog.core.controller;

import com.phoenix.blog.core.service.MessageService;
import com.phoenix.blog.core.service.impl.MessageServiceImpl;
import com.phoenix.blog.model.vo.ArticleMessageVO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    final MessageService messageService;

    @GetMapping("/article/{messageId}")
    public ResultVO getMessageByMessageId(@PathVariable String messageId){
        ArticleMessageVO articleMessageVO = messageService.getMessageByMessageId(messageId);
        return ResultVO.success("Message load success",articleMessageVO);
    }

    @GetMapping("/article/{receiverId}")
    public ResultVO getMessageListByReceiverId(@PathVariable String receiverId){
        return ResultVO.success("Messages load success",messageService.getMessageListByReceiverId(receiverId));
    }
}
