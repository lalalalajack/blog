package org.example.controller;

import org.example.domain.ResponseResult;
import org.example.domain.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 个人中心的当前用户信息
     * 需携带token头
     * @return userInfoVo
     */
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    /**
     * 更新个人信息
     * @param user 待更新的个人信息
     * @return code,msg
     */
    @PutMapping ("/userInfo")
    public  ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateInfo(user);
    }

}
