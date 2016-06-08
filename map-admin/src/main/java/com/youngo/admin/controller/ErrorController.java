/**
 * @Title: ErrorController.java
 * @Package com.yjh.admin.controller
 * @Description: 错误页面统一处理
 * @author yiyan
 * @date 2014-12-9 上午10:34:56
 * @version
 */
package com.youngo.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 类名称：ErrorController
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-12-9 上午10:34:56
 * 修改人：yiyan
 * 修改时间：2014-12-9 上午10:34:56
 * 修改备注：
 * 
 * @version
 */
@Controller
public class ErrorController
{
    private final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    /**
     * @return
     */
    @RequestMapping(value = "/error/404")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String show404Page()
    {
        logger.debug("Rendering 404 page");
        return "error/404";
    }

    /**
     * @return
     */
    @RequestMapping(value = "/error/403")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String show403Page()
    {
        logger.debug("Rendering 403 page");
        return "error/403";
    }

    /**
     * @return
     */
    @RequestMapping(value = "/error/error")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String showInternalServerErrorPage()
    {
        logger.debug("Rendering internal server error page");
        return "error/error";
    }

    /**
     * 拦截所有找不到的请求，返回404页面
     * 
     * @return
     */
    @RequestMapping("/*")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String showNoHandlerFoundPage()
    {
        logger.debug("Rendering 404 page");
        return "error/404";
    }

}
