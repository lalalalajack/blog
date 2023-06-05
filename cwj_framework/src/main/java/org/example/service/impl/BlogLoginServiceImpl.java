package org.example.service.impl;

import org.example.domain.ResponseResult;
import org.example.domain.dto.UserDto;
import org.example.domain.entity.LoginUser;
import org.example.domain.vo.BlogUserLoginVo;
import org.example.domain.vo.UserInfoVo;
import org.example.service.BlogLoginService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.JwtUtil;
import org.example.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
   @Autowired
   private AuthenticationManager authenticationManager;

   @Autowired
   private RedisCache redisCache;

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
        redisCache.setCacheObject("blogLogin:"+userId,loginUser);
        //封装返回token和userInfo
        //通过bean拷贝把User 转为 UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
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
        redisCache.deleteObject("blogLogin:"+userId);
        return ResponseResult.okResult();
    }
}
