/**
 * @Title: AjaxRequestMatcher.java
 * @Package com.yjh.admin.security
 * @Description: 解决超时情况下 Ajax请求安全不通过的问题
 * @author yiyan
 * @date 2014-12-13 上午11:11:37
 * @version
 */
package com.youngo.admin.security;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.RequestMatcher;

/**
 * 类名称：AjaxRequestMatcher
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-12-13 上午11:11:37
 * 修改人：yiyan
 * 修改时间：2014-12-13 上午11:11:37
 * 修改备注：
 * 
 * @version
 */
public class AjaxRequestMatcher implements RequestMatcher
{

    /*
     * (non-Javadoc)
     * <p>Title: matches</p>
     * <p>Description: </p>
     * @param arg0
     * @return
     * @see
     * org.springframework.security.web.util.RequestMatcher#matches(javax.servlet
     * .http.HttpServletRequest)
     */
    @Override
    public boolean matches(HttpServletRequest request)
    {
        System.out.println(Collections.list(request.getHeaderNames()));
        return request.getHeader("X-Requested-With") != null;
    }

}
