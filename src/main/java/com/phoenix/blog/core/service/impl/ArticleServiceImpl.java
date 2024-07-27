package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.cache.RedisCacheHandler;
import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.constant.SortConstant;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.manager.ArticleTagManager;
import com.phoenix.blog.core.manager.CommentManager;
import com.phoenix.blog.core.manager.UserManager;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.core.service.ArticleService;
import com.phoenix.blog.core.service.MessageService;
import com.phoenix.blog.enumeration.MessageType;
import com.phoenix.blog.exceptions.clientException.NotFoundException;
import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.exceptions.clientException.ArticleFormatException;
import com.phoenix.blog.exceptions.clientException.InvalidateArgumentException;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.model.pojo.LinkedConcurrentMap;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    //todo 不要直接注入其他mapper,注入manager或者service
    final ArticleMapper articleMapper;
    final MessageService messageService;

    final ArticleTagManager articleTagManager;
    final UserManager userManager;
    final CommentManager commentManager;

    final RedisCacheHandler redisCacheHandler;
    static final LinkedConcurrentMap<String,ReentrantLock> articleStaticsLockPool = new LinkedConcurrentMap<>();

    @Override
    public ArticleVO getArticleVOById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article;
        User user;

        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try{
            //获取缓存
            article = (Article) redisCacheHandler.getCache(articleId,Article.class);
            if (article == null){
                article = articleMapper.selectById(articleId);
                redisCacheHandler.setCache(articleId, article);
            }
            user = userManager.select(article.getArticleUserId());
            //更新阅读量
            article.setArticleReadCount(article.getArticleReadCount()+1);
            articleMapper.updateById(article);
        }finally {
            reentrantLock.unlock();
        }
        return ArticleVO.buildVO(article,user);
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
    public ArticleVO SaveArticleByUser(ArticleDTO articleDTO) {
        Article article = new Article();

        article.setArticleUserId(articleDTO.getArticleUserId())
        .setArticleTitle(articleDTO.getArticleTitle())
        .setArticleContent(articleDTO.getArticleContent())
        .setArticleReviseTime(new Timestamp(System.currentTimeMillis()));

        if (DataUtil.isEmptyData(articleDTO.getArticleTitle())){
            throw new ArticleFormatException(RespMessageConstant.ARTICLE_TITLE_EMPTY_ERROR);
        }

        if (DataUtil.isEmptyData(articleDTO.getArticleContent())){
            throw new ArticleFormatException(RespMessageConstant.ARTICLE_CONTENT_EMPTY_ERROR);
        }

        articleMapper.insert(article);

        User user = userManager.select(article.getArticleUserId());
        return ArticleVO.buildVO(article,user);
    }

    @Override
    public void updateArticleContent(ArticleDTO articleDTO) {
        String articleId = articleDTO.getArticleId();
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
        reentrantLock.lock();
        try {
            redisCacheHandler.deleteCache(articleId);

            Article article = articleMapper.selectById(articleId);
            if (article == null) throw new NotFoundException(RespMessageConstant.ARTICLE_NOT_FOUND_ERROR);

            article.setArticleTitle(articleDTO.getArticleTitle())
                    .setArticleContent(articleDTO.getArticleContent())
                    .setArticleReviseTime(new Timestamp(System.currentTimeMillis()));
            articleMapper.updateById(article);
        }finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void updateArticleStatics(ArticleDTO articleDTO) {
        ReentrantLock reentrantLock = articleStaticsLockPool.getIfAbsent(articleDTO.getArticleId(), ReentrantLock.class);
        reentrantLock.lock();
        try {
            String articleId = articleDTO.getArticleId();

            if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

            Article article = articleMapper.selectById(articleId);
            if (article == null) throw new NotFoundException(RespMessageConstant.ARTICLE_NOT_FOUND_ERROR);

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
            reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, ReentrantLock.class);
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
        if (article == null) throw new NotFoundException(RespMessageConstant.ARTICLE_NOT_FOUND_ERROR);

        articleTagManager.deleteBatch(articleTagManager.selectListByArticleId(articleId));
        articleMapper.delete(new QueryWrapper<Article>().eq("article_id",articleId));
    }
}
