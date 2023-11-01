package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Article;
import org.example.dao.ArticleDao;
import org.example.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2023-06-01 13:26:53
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    /**
     * 查询热门文章
     * 需要查询浏览量最高的前10篇文章的信息
     * @return 展示文章标题和浏览量以及链接
     */
    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //正式文章 Entity 对象封装操作类
        queryWrapper.eq(Article::getStatus,0);
        //按照浏览量排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //只显示10条（采用分页方式实现） 简单分页模型作为翻页对象（ipage)
        Page<Article> ipage = new Page<>(1,10);
        page(ipage,queryWrapper);

        List<Article> articles = ipage.getRecords();

        return ResponseResult.okResult(articles);
    }
}

