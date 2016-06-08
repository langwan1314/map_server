/**
 * @Title: ExceptionHandlingAdvice.java
 * @Package com.yjh.admin.common.advice
 * @Description: 后台管理系统异常统一处理
 * 1. 返回JSON的请求，方法中带 produces =
 * "application/json"，如果有异常，必须封装成RestException再抛出。
 * 2. 返回页面的，不要求特殊处理。如果需要自定义code,可以封装成GenericException。
 * @author yiyan
 * @date 2014-12-9 上午10:20:10
 * @version
 */
package com.youngo.core.exception;

import com.youngo.core.model.Result;

import org.nutz.ssdb4j.spi.SSDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@ControllerAdvice
public class ExceptionHandlingAdvice
{
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({RestException.class})
    public Result handleBadRequest(final RestException restException, final WebRequest request)
    {
        logger.info(restException.getMessage(), restException);
        Result result = new Result();
        result.setCode(restException.getCode());
        result.setMsg(restException.getMessage());
        return result;
    }
    
    public Result handleBadRequest(final SSDBException ssdbException, final WebRequest request)
    {
        logger.error(ssdbException.getMessage(), ssdbException);
        Result result = new Result();
        result.setCode(Result.ssdbException_code);
        result.setMsg(Result.ssdbException_msg);
        //TODO 需要针对缓存异常做不同的处理
        return result;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler
    public Result handleDefault(Exception exception, HttpServletRequest request, HttpServletResponse response)
    {
        logger.error(exception.getMessage(), exception);
        Result result = new Result();
        result.setCode(Result.param_error);
        result.setMsg(exception.getMessage());
        result.setData(ExceptionUtil.getStackTrace(exception));
        return result;
    }

}
