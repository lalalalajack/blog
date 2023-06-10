package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-06-09 16:00:33
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

