package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.constant.SortConstant;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.core.service.ArticleService;
import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.exceptions.clientException.ArticleFormatException;
import com.phoenix.blog.exceptions.clientException.ArticleNotFoundException;
import com.phoenix.blog.exceptions.clientException.InvalidateArgumentException;
import com.phoenix.blog.model.pojo.LinkedConcurrentMap;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;



@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    final ArticleMapper articleMapper;
    static final LinkedConcurrentMap<String,ReentrantLock> articleStaticsLockPool = new LinkedConcurrentMap<>();

    @Override
    public ArticleVO getArticleById(String articleId) {

        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();
        ArticleVO articleVO = articleMapper.selectArticleWithPublisher(articleId);
        if (articleVO == null){
            throw new ArticleNotFoundException();
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
        }catch (Exception e){
            //Todo:处理异常
            return;
        }
        LockSupport.unpark(Thread.currentThread());
        reentrantLock.lock();
        try {
            String articleId = articleDTO.getArticleId();

            if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

            Article article = articleMapper.selectById(articleId);
            if (article == null) throw new ArticleNotFoundException();

            int newUpvoteCount = article.getArticleUpvoteCount()+articleDTO.getArticleUpvoteCountChange();
            int newBookmarkCount = article.getArticleBookmarkCount()+articleDTO.getArticleBookmarkCountChange();

            DataUtil.setFields(article, articleDTO, () ->
                    article.setArticleReadCount(article.getArticleReadCount()+1)
                            .setArticleUpvoteCount(newUpvoteCount)
                            .setArticleBookmarkCount(newBookmarkCount)
            );
            articleMapper.updateById(article);
        }finally{
            reentrantLock.unlock();
        }
    }

    @Override
    public void updateArticleBookmarkCount(String articleId, int bookmarkCountChange) {
        ReentrantLock reentrantLock;
        try {
            reentrantLock = articleStaticsLockPool.getIfAbsent(articleId, new ReentrantLock());
        }catch (Exception e){
            //Todo:处理异常
            return;
        }
        reentrantLock.lock();
        try {
            Article article = articleMapper.selectById(articleId);
            article.setArticleBookmarkCount(article.getArticleBookmarkCount()+ bookmarkCountChange);
            articleMapper.updateById(article);
        }finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void deleteArticleById(String articleId) {
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();

        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new ArticleNotFoundException();

        articleMapper.delete(new QueryWrapper<Article>().eq("article_id",articleId));
    }


}
