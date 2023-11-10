package org.example.service.impl;

import org.example.domain.ResponseResult;
import org.example.domain.dto.UserDto;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.User;
import org.example.domain.vo.AdminUserInfoVo;
import org.example.domain.vo.MenuVo;
import org.example.domain.vo.RoutersVo;
import org.example.domain.vo.UserInfoVo;
import org.example.service.LoginService;
import org.example.service.MenuService;
import org.example.service.RoleService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.JwtUtil;
import org.example.utils.RedisCache;
import org.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {
   @Autowired
   private AuthenticationManager authenticationManager;

   @Autowired
   private RedisCache redisCache;

   @Autowired
   private MenuService menuService;

   @Autowired
   private RoleService roleService;

    /**
     * 登录业务方法
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult login(UserDto userDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUserName(),userDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取用户id
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //生成jwt
        String jwt = JwtUtil.createJWT(userId);
        //存入用户信息到redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //封装返回token
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    /**
     * 登出业务方法
     * @return
     */
    @Override
    public ResponseResult logout() {
        //获取token，获取loginUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }

    /**
     * 获取当前信息接口
     * @return 封装后的AdminUserInfoVo：UserInfoVo+roles+permissions
     */
    @Override
    public ResponseResult<AdminUserInfoVo> getInfo() {
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //封装数据返回

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    /**
     * 返回用户所能访问的菜单数据。（有token）
     * @return 封装后的RoutersVo：List<Menu>
     */
    @Override
    public ResponseResult<RoutersVo> getRouters() {
        //获得当前登陆的用户id
        Long userId = SecurityUtils.getUserId();
        //查询menu,结果是tree的形式
        List<MenuVo> menuVos = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menuVos));
    }
}
