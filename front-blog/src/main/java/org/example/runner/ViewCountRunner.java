package org.example.runner;

import org.example.dao.ArticleDao;
import org.example.domain.entity.Article;
import org.example.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 实现CommandLineRunner接口，在应用启动时初始化缓存
 * 在应用启动时把博客的浏览量存储到redis中
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息  id  viewCount
        List<Article> articles = articleDao.selectList(null);

//        Map<String, Integer> viewCountMap = new HashMap<>();
//
//        for (Article article : articles) {
//            String key = article.getId().toString();
//            int value = article.getViewCount().intValue();
//            viewCountMap.put(key,value);
//        }

        //stream流写法
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(
                        article -> article.getId().toString(),
                        article -> article.getViewCount().intValue()//如果Long类型（1L）存在redis中无法进行递增操作
                ));


        //存储到redis中
        redisCache.setCacheMap("article:viewCount", viewCountMap);
    }
}


