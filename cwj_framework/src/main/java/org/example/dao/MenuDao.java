package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-09 16:00:00
 */
public interface MenuDao extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}

