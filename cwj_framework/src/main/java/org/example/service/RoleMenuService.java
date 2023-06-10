package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.RoleMenu;

/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-06-10 23:04:37
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleByRoleId(Long id);
}

