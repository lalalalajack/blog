package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.UserDao;
import org.example.domain.ResponseResult;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.User;
import org.example.domain.vo.UserInfoVo;
import org.example.service.UserService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-06-05 23:49:46
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    /**
     * 个人中心的当前用户信息
     * 需携带token头
     * @return userInfoVo
     */
    @Override
    public ResponseResult userInfo() {
        //获取当用户id
        Long userId = SecurityUtils.getUserId();
        //通过用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    /**
     * 更新个人信息
     * @param user 待更新的个人信息
     * @return code,msg
     */
    @Override
    public ResponseResult updateInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }
}

