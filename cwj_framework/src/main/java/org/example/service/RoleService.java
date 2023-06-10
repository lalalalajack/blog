package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-06-10 22:46:20
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    List<Role> selectRoleAll();

    void updateRole(Role role);

    void insertRole(Role role);

    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);
}

