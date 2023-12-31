package org.example.service;

import org.example.domain.ResponseResult;
import org.example.domain.dto.UserDto;

/**
 * 前台用户登陆业务
 */
public interface BlogLoginService {
    ResponseResult login(UserDto userDto);

    ResponseResult logout();
}
