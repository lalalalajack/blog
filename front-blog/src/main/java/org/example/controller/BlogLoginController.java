package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "登陆登出",description = "登录登出相关接口")
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    /**
     * 登录接口
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登陆",notes = "")
    public ResponseResult login(@RequestBody UserDto userDto){
        //TODO 使用DTO来校验
        if(!StringUtils.hasText(userDto.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(userDto);
    }

    /**
     * 登出接口
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "登出",notes = "")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
