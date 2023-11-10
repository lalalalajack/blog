package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.example.domain.ResponseResult;
import org.example.domain.dto.UserDto;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.User;
import org.example.domain.vo.AdminUserInfoVo;
import org.example.domain.vo.RoutersVo;
import org.example.domain.vo.UserInfoVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.service.LoginService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "登陆",description = "登陆登出相关接口")
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 用户登陆接口
     * @param user userDto
     * @return 封装好的token
     */
    @PostMapping("/user/login")
    @ApiOperation(value = "用户登陆接口",notes = "用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",value = "账号密码DTO")
    })
    public ResponseResult login(@RequestBody UserDto user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    /**
     * 获取当前信息接口（有token）
     * @return 封装后的AdminUserInfoVo：UserInfoVo+roles+permissions
     */
    @GetMapping("/getInfo")
    @ApiOperation(value = "获取当前用户信息",notes = "得到当前用户信息、权限信息、角色信息")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        return loginService.getInfo();
    }

    /**
     * 返回用户所能访问的菜单数据。（有token）
     * @return 封装后的RoutersVo：List<Menu>
     */
    @GetMapping("/getRouters")
    @ApiOperation(value = "返回用户所能访问的菜单数据",notes = "如果用户id为1代表管理员，menus中需要有所有菜单类型为C或者M的，状态为正常的，未被删除的权限")
    public ResponseResult<RoutersVo> getRouters(){
        return loginService.getRouters();
    }
}
