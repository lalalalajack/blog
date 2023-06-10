package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.RoleDao;
import org.example.domain.entity.Role;
import org.example.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-06-09 16:00:33
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

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
}

