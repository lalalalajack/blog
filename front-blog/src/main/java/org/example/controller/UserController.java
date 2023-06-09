package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户",description = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 个人中心的当前用户信息
     * 需携带token头
     * @return userInfoVo
     */
    @GetMapping("/userInfo")
    @ApiOperation(value = "获取个人中心的用户信息",notes = "")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    /**
     * 更新用户信息
     * @param user 待更新的用户信息
     * @return code,msg
     */
    @PutMapping ("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    @ApiOperation(value = "更新用户信息",notes = "")
    public  ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateInfo(user);
    }


    /**
     * 用户注册
     * @param user 用户信息
     * @return code,msg
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes = "")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

}
