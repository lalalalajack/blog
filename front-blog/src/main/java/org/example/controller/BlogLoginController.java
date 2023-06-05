package org.example.controller;

import org.example.domain.ResponseResult;
import org.example.domain.dto.UserDto;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserDto userDto){
        if(StringUtils.hasText(userDto.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(userDto);
    }
}
