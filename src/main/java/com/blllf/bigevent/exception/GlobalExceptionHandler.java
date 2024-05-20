package com.blllf.bigevent.exception;

import com.blllf.bigevent.pojo.Result;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ControllerAdvice 注解是用来捕获Controller中抛出的指定类型的异常，从而实现不同类型的异常统一处理
 * */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
    }
}
