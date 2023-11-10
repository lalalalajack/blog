package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.Menu;
import org.example.domain.vo.MenuVo;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-06-09 16:00:00
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> selectMenuList(Menu menu);

    boolean hasChild(Long menuId);

    List<Long> selectMenuListByRoleId(Long roleId);
}

