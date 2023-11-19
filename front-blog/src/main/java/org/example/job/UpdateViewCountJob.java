package org.example.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.example.domain.entity.Article;
import org.example.service.ArticleService;
import org.example.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时任务，每五分钟将redis的varticle:viewCount更新到数据库中
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    /**
     * 每隔五分钟
     * 0 0/5 * * * ?
     * 每隔5秒钟
     * 0/5 * * * * ?
     */
    @Scheduled(cron = "0/60 * * * * ?")
    public void updateViewCount() {
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //双列集合不能直接进行流处理，必须先转化为单列集合，比如entrySet
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        //articleService.updateBatchById(articles);
        for (Article article : articles) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getId,article.getId());
            updateWrapper.set(Article::getViewCount,article.getViewCount());
            articleService.update(updateWrapper);
        }

    }
}
