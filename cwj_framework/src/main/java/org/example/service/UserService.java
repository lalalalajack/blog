package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-06-05 23:49:46
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateInfo(User user);

    ResponseResult register(User user);
}

