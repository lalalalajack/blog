package org.example.service;

import org.example.domain.ResponseResult;
import org.example.domain.dto.UserDto;

/**
 * 后台用户登陆业务
 */
public interface LoginService {
    ResponseResult login(UserDto userDto);

    ResponseResult logout();
}
