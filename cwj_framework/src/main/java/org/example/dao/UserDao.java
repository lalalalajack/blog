package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.User;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-04 23:04:22
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}

