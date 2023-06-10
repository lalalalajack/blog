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

    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(User user);

    boolean checkEmailUnique(User user);

    ResponseResult addUser(User user);

    void updateUser(User user);
}

