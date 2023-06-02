package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Category;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-06-01 18:14:22
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}

