package org.example.service;

import org.example.domain.ResponseResult;
import org.example.domain.dto.UserDto;

public interface BlogLoginService {
    ResponseResult login(UserDto userDto);

    ResponseResult logout();
}
