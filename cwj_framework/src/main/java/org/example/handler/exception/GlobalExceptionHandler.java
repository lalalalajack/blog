package org.example.handler.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.ResponseResult;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.springframework.web.bind.annotation.*;

/**
 * 自定义的全局异常处理类
 * @ControllerAdvice 异常处理 本质控制器增强
 * @ResponseBody 将方法返回值放入响应体中
 * @RestControllerAdvice = @ControllerAdvice+@ResponseBod
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！{}",e);
        //从异常信息中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！{}",e);
        //从异常信息中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}
