package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.UserRoleDao;
import org.example.domain.entity.UserRole;
import org.example.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-06-11 00:18:38
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

}

