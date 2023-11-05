package org.example.handler.security;

import com.alibaba.fastjson.JSON;
import org.example.domain.ResponseResult;
import org.example.enums.AppHttpCodeEnum;
import org.example.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的认证失败处理器
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    //InsufficientAuthenticationException 不足认证异常
    //BadCredentialsException 错误凭证异常

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //打印异常信息
        e.printStackTrace();
        //判断异常类别
        ResponseResult result = null;
        if(e instanceof BadCredentialsException){
            result = ResponseResult.okResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),e.getMessage());

        } else if (e instanceof InsufficientAuthenticationException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else{
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"认证或授权失败");
        }
        //响应给前端
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));

    }
}
