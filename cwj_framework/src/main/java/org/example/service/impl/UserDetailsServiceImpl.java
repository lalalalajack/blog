package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.constant.SystemConstants;
import org.example.dao.MenuDao;
import org.example.dao.UserDao;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.User;
import org.example.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuDao menuDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userDao.selectOne(queryWrapper);
        //判断是否查到用户 如果没查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //根据用户查询权限信息 添加到LoginUser中
        //如果是后台用户，才需要查询权限封装
        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> perms = menuDao.selectPermsByUserId(user.getId());
            return new LoginUser(user,perms);
        }

        return new LoginUser(user,null);
    }
}
