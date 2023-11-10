package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.constant.SystemConstants;
import org.example.dao.MenuDao;
import org.example.domain.entity.Menu;
import org.example.domain.vo.MenuVo;
import org.example.service.MenuService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-06-09 16:00:01
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回menus中需要有所有菜单类型为C或者M的，状态为正常的，未被删除的权限
        if(id==1L){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            //菜单类型为C或者M的
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            //状态为正常的
            queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        /**
         * 如果不是管理员，需要userId -> 查userRole表 -> roleId -> 查roleMenus表
         * ->menusId -> 查menus表 -> 获得perms  = 三表联查 来返回当前用户具有的权限
         */
        return getBaseMapper().selectPermsByUserId(id);


    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        //获取 菜单权限表(Menu)表数据库访问层 对象
        MenuDao menuDao = getBaseMapper();
        List<Menu> menus = null;
        //判断是否管理员
        if(SecurityUtils.isAdmin()){
            //如果是 返回所有符合要求 的 menu
            //menus中需要有所有菜单类型为C或者M的，状态为正常的，未被删除的权限
            menus=menuDao.selectAllRouterMenu();
        }else{
            //否则 获取当前用户所具有的menu
            menus=menuDao.selectRouterMenuTreeByUserId(userId);
        }

        //将Menu -> MenuVo(增加了children，删减了部分字段）
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);

        //构建tree
        //先找出第一层的菜单，然后去找他们的子菜单设置到children属性中
        List<MenuVo> menuTree = buildMenuTree(menuVos,0L);
        return menuTree;
    }

    private List<MenuVo> buildMenuTree(List<MenuVo> menus, long parentId) {
        List<MenuVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数（children） 的子Menu集合
     * @param menu 待存入参数（children）的menu
     * @param menus 从总的menu集合里去寻找待存入参数的menu的子集
     * @return 都存入参数的menu集合
     */
    private List<MenuVo> getChildren(MenuVo menu,List<MenuVo> menus){
        List<MenuVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                //假设有第三层子菜单
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}

