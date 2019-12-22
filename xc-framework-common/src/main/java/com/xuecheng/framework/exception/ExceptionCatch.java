package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice//控制器增强
public class ExceptionCatch {
    //定义一个日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //定义一个MAP集合,配置异常类所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义一个map构建对象builder,去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    //静态代码块
    static {
        //定义了异常类型所对应的代码
        builder.put(HttpMessageNotReadableException.class, CommonCode.ILLEGAL_PARAMETER);
    }

    //捕获此类异常,自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult fetchException(CustomException custom) {
        //记录日志
        LOGGER.error("catch exception:{}", custom.getMessage());
        ResultCode resultCode = custom.getResultCode();
        return new ResponseResult(resultCode);
    }

    //捕获未知异常异常,自定义异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception custom) {
        //记录日志
        LOGGER.error("catch exception:{}", custom.getMessage());
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();// 异常集合构建成功
        }
        //从EXCEPTIONS中去寻找所对应的异常错误代码
        ResultCode resultCode = EXCEPTIONS.get(custom.getClass());
        if (resultCode != null) {
            //找到了就响应给用户
            return new ResponseResult(resultCode);
        } else {
            //没有就相应通用异常代码
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }
}
