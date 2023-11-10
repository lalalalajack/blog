package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.CategoryDao;
import org.example.domain.ResponseResult;
import org.example.constant.SystemConstants;
import org.example.domain.entity.Article;
import org.example.domain.entity.Category;
import org.example.domain.vo.CategoryVo;
import org.example.domain.vo.PageVo;
import org.example.service.ArticleService;
import org.example.service.CategoryService;
import org.example.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-06-01 18:14:22
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询分类 页面上需要展示分类列表，用户可以点击具体的分类查看该分类下的文章列表
     * 返回值：在展示有发布正式文章的分类，必须是正常状态的分类
     * @return
     */
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已发布的文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleListlist = articleService.list(queryWrapper);
        //获取文章的分类id，并且去重 (使用函数式编程）
        Set<Long> categoryIds  = articleListlist.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 查询所有分类接口
     * @return
     */
    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }


    @Override
    public PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(category.getName()),Category::getName, category.getName());
        queryWrapper.eq(Objects.nonNull(category.getStatus()),Category::getStatus, category.getStatus());

        Page<Category> iPage = new Page<>(pageNum,pageSize);
        page(iPage,queryWrapper);

        //转换成VO
        List<Category> categories = iPage.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(iPage.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }
}

