/**
 * @Title: HomeController.java
 * @Package com.yjh.admin.controller
 * @Description: 主页控制器
 * @author yiyan
 * @date 2014-11-26 上午11:25:39
 * @version
 */
package com.youngo.admin.controller;

import java.util.Locale;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类名称：HomeController
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-26 上午11:25:39
 * 修改人：yiyan
 * 修改时间：2014-11-26 上午11:25:39
 * 修改备注：
 * 
 * @version
 */
@Controller
public class HomeController
{

    @RequestMapping("/")
    public String welcome(Model model, Locale locale)
    {
        String returnView = "redirect:/version/list";

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String)
        {
            if (((String) principal).equalsIgnoreCase("anonymousUser"))
            {
                returnView = "login";
            }
        }
        else if (principal instanceof User)
        {
            User user = (User) principal;
            if (!user.isAccountNonExpired() && !user.isAccountNonLocked() && !user.isCredentialsNonExpired()
                    && !user.isEnabled())
            {
                returnView = "login";
            }
        }

        return returnView;

    }
}
