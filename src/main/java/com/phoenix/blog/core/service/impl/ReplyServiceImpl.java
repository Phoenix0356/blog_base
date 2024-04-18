package com.phoenix.blog.core.service.impl;

import com.phoenix.blog.core.mapper.ReplyMapper;
import com.phoenix.blog.core.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyMapper replyMapper;
}
