/**
 * @Title: ServletUtils.java
 * @Package com.yjh.core.common
 * @Description: Servlet 工具类
 * @author yiyan
 * @date 2014-12-13 下午3:01:37
 * @version
 */
package com.youngo.core.common;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 类名称：ServletUtils
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-12-13 下午3:01:37
 * 修改人：yiyan
 * 修改时间：2014-12-13 下午3:01:37
 * 修改备注：
 * 
 * @version
 */
public class ServletUtils
{
    /**
     * @param request
     * @return
     */
    public static String getRelativeUrl(HttpServletRequest request)
    {
        String baseUrl = null;

        if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
            baseUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        else
            baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();

        StringBuffer buf = request.getRequestURL();

        if (request.getQueryString() != null)
        {
            buf.append("?");
            buf.append(request.getQueryString());
        }

        return buf.substring(baseUrl.length());
    }

    /**
     * @param request
     * @return
     */
    public static String getBaseUrl(HttpServletRequest request)
    {
        if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
            return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        else
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
    }

    /**
     * @param request
     * @param path
     * @return
     */
    public static File getRealFile(HttpServletRequest request, String path)
    {
        return new File(request.getSession().getServletContext().getRealPath(path));
    }

    /**
     * 获取请求信息
     * 
     * @param request
     * @return
     */
    public static String getParameterInfo(HttpServletRequest request)
    {
        StringBuffer sb = new StringBuffer();

        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();)
        {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++)
            {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            sb.append("," + name + "=" + valueStr);
        }

        return sb.toString().replaceFirst(",", "") + "]";
    }
}
