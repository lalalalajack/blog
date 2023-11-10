package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.ResponseResult;
import org.example.constant.SystemConstants;
import org.example.domain.dto.AddArticleDto;
import org.example.domain.entity.Article;
import org.example.dao.ArticleDao;
import org.example.domain.entity.ArticleTag;
import org.example.domain.entity.Category;
import org.example.domain.vo.ArticleDetailVo;
import org.example.domain.vo.ArticleListVo;
import org.example.domain.vo.HotArticleVo;
import org.example.domain.vo.PageVo;
import org.example.service.ArticleService;
import org.example.service.ArticleTagService;
import org.example.service.CategoryService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2023-06-01 13:26:53
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 查询热门文章
     * 需要查询浏览量最高的前10篇文章的信息
     * @return 展示文章标题和浏览量以及链接
     */
    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //正式文章 Entity 对象封装操作类
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //只显示10条（采用分页方式实现） 简单分页模型作为翻页对象（ipage)
        Page<Article> iPage = new Page<>(1,10);
        page(iPage,queryWrapper);
        List<Article> articles = iPage.getRecords();

        //从redis中获取viewCount
        for (Article article : articles) {
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            article.setViewCount(Long.valueOf(viewCount));
        }

        //bean拷贝  原理利用字段名字相同
//       List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo hotArticleVo = new HotArticleVo();
//            BeanUtils.copyProperties(article,hotArticleVo);
//            articleVos.add(hotArticleVo);
//        }
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    /**
     * 分页查询文章列表：在首页和分类页面都需要查询文章列表。
     * 首页：查询所有的文章
     * 分类页面：查询对应分类下的文章
     * 只能查询正式发布的文章，置顶的文章要显示在最前面
     * @param pageNum 当前页码
     * @param pageSize 页面大小
     * @param categoryId 分类id
     * @return pageVo 对象
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId，就要进行查询
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //状态正式发布
        queryWrapper.eq(Article::getStatus,SystemConstants.STATUS_NORMAL);
        //置顶文章显示最前
        queryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> iPage = new Page<>(pageNum,pageSize);
        //执行查询
        page(iPage,queryWrapper);

        List<Article> articles = iPage.getRecords();

        //从redis中获取viewCount
        for (Article article : articles) {
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            article.setViewCount(Long.valueOf(viewCount));
        }


        //查询categoryName
        //方法一 categoryId去查询categoryName 进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
        //方法二：stream流 获取分类id,查询分类信息，获取分类名称,把分类名称设置给article
         articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());


        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(iPage.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,iPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 文章详情 在文章列表点击阅读全文时能够跳转到文章详情页面，可以让用户阅读文章正文。
     * @param id 文章id
     * @return ArticleDetailVo对象
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(Long.valueOf(viewCount));
//        //转换成VO
//        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //查询文章结果，附加分类名
        Long categoryId = article.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            article.setCategoryName(category.getName());
        }
        // 封装查询结果
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        return ResponseResult.okResult(articleDetailVo);
    }


    /**
     * 更新浏览量时更新redis数据
     * @param id 更新的文章id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应的id浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    /**
     * 新增博文
     * @param articleDto
     * @return
     */
    @Override
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        //获得 文章id和标签id 映射关系
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return null;
    }
}

