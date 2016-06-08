/**
 * @Title: SecurityUtil.java
 * @Package com.yjh.admin.common
 * @Description: 安全帮助类
 * @author yiyan
 * @date 2015-2-10 下午3:42:50
 * @version
 */
package com.youngo.admin.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * 类名称：SecurityUtil
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2015-2-10 下午3:42:50
 * 修改人：yiyan
 * 修改时间：2015-2-10 下午3:42:50
 * 修改备注：
 * 
 * @version
 */
public class SecurityUtil
{
    public static String getUserName()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User)
        {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getUsername();
        }
        else
        {
            return principal.toString();
        }
    }

}
