package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-09 16:00:33
 */
public interface RoleDao extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

