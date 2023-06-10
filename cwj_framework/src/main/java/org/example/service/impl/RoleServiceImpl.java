package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.constant.SystemConstants;
import org.example.dao.RoleDao;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Role;
import org.example.domain.entity.RoleMenu;
import org.example.domain.vo.PageVo;
import org.example.service.RoleMenuService;
import org.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-06-10 22:46:20
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是返回集合中只需要有admin
        if(id==1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        /**
         * 如果不是管理员，需要userId -> 查userRole表 -> roleId -> 查role表 -> role.key
         * 两张表连表查询
         */
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public List<Role> selectRoleAll() {
        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, SystemConstants.STATUS_NORMAL));
    }

    @Override
    public void updateRole(Role role) {
        updateById(role);
        //同时更新角色菜单 先删除
        roleMenuService.deleteRoleByRoleId(role.getId());
        //后插入
        insertRoleMenu(role);
    }

    @Override
    public void insertRole(Role role) {
        save(role);
        System.out.println(role.getId());
        //存在R-M映射
        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMenu(role);
        }
    }

    @Override
    public ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(role.getRoleName()), Role::getRoleName, role.getRoleName());
        queryWrapper.eq(StringUtils.hasText(role.getStatus()),Role::getStatus,role.getRoleName());
        queryWrapper.orderByAsc(Role::getRoleSort);

        Page<Role> iPage = new Page<>(pageNum,pageSize);
        page(iPage,queryWrapper);

        //转为VO
        List<Role> roles = iPage.getRecords();

        PageVo pageVo = new PageVo(roles, iPage.getTotal());
        return  ResponseResult.okResult(pageVo);
    }

    private void insertRoleMenu(Role role) {
        //从role中获取出映射的menuIds，并为其创建每一个roleMenu对象
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }

}

