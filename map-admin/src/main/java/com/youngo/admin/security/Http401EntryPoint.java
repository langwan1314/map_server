/**
 * @Title: Http401EntryPoint.java
 * @Package com.yjh.admin.security
 * @Description: Ajax 安全控制类
 * @author yiyan
 * @date 2014-12-13 上午11:10:10
 * @version
 */
package com.youngo.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngo.core.common.ServletUtils;
import com.youngo.core.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Http401EntryPoint implements AuthenticationEntryPoint
{

    /**
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException
    {
        Result result = new Result();
        result.setCode(HttpStatus.UNAUTHORIZED.value());
        result.setMsg("UNAUTHORIZED");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("url", ServletUtils.getBaseUrl(request) + "/login");
        result.setData(data);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        ObjectMapper objectMapper = new ObjectMapper();

        response.getOutputStream().write(objectMapper.writeValueAsBytes(result));
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
