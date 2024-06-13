package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.constant.SortConstant;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.core.mapper.CommentMapper;
import com.phoenix.blog.core.service.ArticleService;
import com.phoenix.blog.core.service.MessageService;
import com.phoenix.blog.enumeration.MessageType;
import com.phoenix.blog.exceptions.serverException.LockPoolException;
import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.exceptions.clientException.ArticleFormatException;
import com.phoenix.blog.exceptions.clientException.ArticleNotFoundException;
import com.phoenix.blog.exceptions.clientException.InvalidateArgumentException;
import com.phoenix.blog.model.entity.Comment;
import com.phoenix.blog.model.pojo.LinkedConcurrentMap;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;



@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    final ArticleMapper articleMapper;
    final CommentMapper commentMapper;
    final MessageService messageService;
    static final LinkedConcurrentMap<String,ReentrantLock> articleStaticsLockPool = new LinkedConcurrentMap<>();

    @Override
    public ArticleVO getArticleById(String articleId) {

        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();
        ArticleVO articleVO = articleMapper.selectArticleWithPublisher(articleId);
        if (articleVO == null){
            throw new ArticleNotFoundException();
        }

        //增加阅读量
        ReentrantLock reentrantLock;
        try {
            reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, new ReentrantLock());
        }catch (LockPoolException lockPoolException){
            //TODO:记录日志,处理异常
            return null;
        }
        reentrantLock.lock();
        try{
            addArticleReadCount(articleId);
        }finally {
            reentrantLock.unlock();
        }
        return articleVO;
    }

    @Override
    public List<ArticleVO> getArticleAll(int sortStrategy) {
        List<ArticleVO> articleVOList = articleMapper.selectArticleWithPublisherList();

        switch (sortStrategy){
            case (SortConstant.SORT_BY_READ_COUNT):
                articleVOList.sort(Comparator.comparing(ArticleVO::getArticleReadCount,Comparator.reverseOrder()));
                break;
            case (SortConstant.SORT_BY_UPVOTE_COUNT):
                articleVOList.sort(Comparator.comparing(ArticleVO::getArticleUpvoteCount,Comparator.reverseOrder()));
                break;
            default:
                break;
        }

        return articleVOList;
    }

    @Override
    public List<ArticleVO> getArticleUserList(String userId) {
        return articleMapper.selectUserArticleList(userId);
    }

    @Override
    public void SaveArticleByUser(ArticleDTO articleDTO) {
        Article article = new Article();

        DataUtil.setFields(article,articleDTO,() ->
                article.setArticleUserId(articleDTO.getArticleUserId())
                .setArticleTitle(articleDTO.getArticleTitle())
                .setArticleContent(articleDTO.getArticleContent())
                .setArticleReviseTime(new Timestamp(System.currentTimeMillis())));

        if (DataUtil.isEmptyData(articleDTO.getArticleTitle())){
            throw new ArticleFormatException("title is empty");
        }

        if (DataUtil.isEmptyData(articleDTO.getArticleContent())){
            throw new ArticleFormatException("content is empty");
        }

        articleMapper.insert(article);

    }

    @Override
    public void updateArticleContent(ArticleDTO articleDTO) {
        String articleId = articleDTO.getArticleId();

        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new ArticleNotFoundException();

        DataUtil.setFields(article, articleDTO, () ->
                article.setArticleTitle(articleDTO.getArticleTitle())
                        .setArticleContent(articleDTO.getArticleContent())
                        .setArticleReviseTime(new Timestamp(System.currentTimeMillis())));
        articleMapper.updateById(article);
    }

    @Override
    public void updateArticleStatics(ArticleDTO articleDTO) {
        ReentrantLock reentrantLock;
        try {
            reentrantLock = articleStaticsLockPool.getIfAbsent(articleDTO.getArticleId(), new ReentrantLock());
        }catch (LockPoolException lockPoolException){
            //TODO:记录日志
            return;
        }
        reentrantLock.lock();
        try {
            String articleId = articleDTO.getArticleId();

            if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

            Article article = articleMapper.selectById(articleId);
            if (article == null) throw new ArticleNotFoundException();

            int newUpvoteCount = article.getArticleUpvoteCount()+articleDTO.getArticleUpvoteCountChange();
            int newBookmarkCount = article.getArticleBookmarkCount()+articleDTO.getArticleBookmarkCountChange();
            int messageType = articleDTO.getArticleMessageType();

            //判断是否被点赞
            if (DataUtil.isOptionChosen(messageType, MessageType.UPVOTE.getTypeNum())){
                article.setArticleUpvoteCount(newUpvoteCount);
                messageService.saveMessage(articleId,MessageType.UPVOTE,TokenContext.getUserId());
            }

            //判断是否被收藏
            if (DataUtil.isOptionChosen(messageType, MessageType.BOOKMARK.getTypeNum())){
                article.setArticleBookmarkCount(newBookmarkCount);
                messageService.saveMessage(articleId,MessageType.BOOKMARK,TokenContext.getUserId());
            }

            //判断是否被取消收藏
            if (DataUtil.isOptionChosen(messageType, MessageType.BOOKMARK_CANCEL.getTypeNum())){
                article.setArticleBookmarkCount(newBookmarkCount);
                messageService.saveMessage(articleId,MessageType.BOOKMARK_CANCEL,TokenContext.getUserId());
            }

            articleMapper.updateById(article);
        }finally{
            reentrantLock.unlock();
        }
    }

    @Override
    public void deleteArticleBookmarkCount(String articleId) {
        ReentrantLock reentrantLock;
        try {
            reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, new ReentrantLock());
        }catch (Exception e){
            //TODO:记录日志
            return;
        }
        reentrantLock.lock();
        try {
            Article article = articleMapper.selectById(articleId);
            article.setArticleBookmarkCount(article.getArticleBookmarkCount()-1);
            articleMapper.updateById(article);
            messageService.saveMessage(articleId, MessageType.BOOKMARK_CANCEL,TokenContext.getUserId());
        }finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void deleteArticleById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();
        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new ArticleNotFoundException();
        commentMapper.delete(new QueryWrapper<Comment>().eq("comment_article_id",articleId));
        articleMapper.delete(new QueryWrapper<Article>().eq("article_id",articleId));
    }
    @Async("asyncServiceExecutor")
    protected void addArticleReadCount(String articleId){
        Article article = articleMapper.selectById(articleId);
        article.setArticleReadCount(article.getArticleReadCount()+1);
        articleMapper.updateById(article);
    }

}
