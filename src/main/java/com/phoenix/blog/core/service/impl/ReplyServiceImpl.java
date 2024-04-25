package com.phoenix.blog.core.service.impl;

import com.phoenix.blog.core.mapper.ReplyMapper;
import com.phoenix.blog.core.service.ReplyService;
import com.phoenix.blog.model.dto.ReplyDTO;
import com.phoenix.blog.model.entity.Reply;
import com.phoenix.blog.model.vo.ReplyVO;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    final private ReplyMapper replyMapper;


    @Override
    @Transactional
    public ReplyVO getReplyById(String replyId) {
        Reply reply = replyMapper.selectById(replyId);
        return ReplyVO.buildVO(reply);
    }

    @Override
    @Transactional
    public ReplyVO getCommentReplyList(String commentId) {

        return null;
    }

    @Override
    @Transactional
    public void saveReply(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        DataUtil.setFields(reply,replyDTO,()->{
            reply.setReplyUserName(replyDTO.getReplyUsername())
                    .setReplyReceiverName(replyDTO.getReplyReceiverName())
                    .setReplyContent(replyDTO.getReplyContent())
                    .setReplyReviseTime(new Timestamp(System.currentTimeMillis()));
        });

        replyMapper.insert(reply);
    }

    @Override
    @Transactional
    public void updateReply(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        DataUtil.setFields(reply,replyDTO,()->{
            reply.setReplyContent(replyDTO.getReplyContent())
                    .setReplyReviseTime(new Timestamp(System.currentTimeMillis()));
        });

        replyMapper.updateById(reply);
    }

    @Override
    @Transactional
    public void deleteReply(String replyId) {
        replyMapper.deleteById(replyId);
    }
}
