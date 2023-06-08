package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Article;

/**
 * 文章表(Article)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-01 13:26:51
 */
@Mapper
public interface ArticleDao extends BaseMapper<Article> {

}

