/**
 * @Title: LoginController.java
 * @Package com.yjh.admin.controller
 * @Description: 登录控制器
 * @author yiyan
 * @date 2014-11-26 下午2:57:04
 * @version
 */
package com.youngo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 类名称：LoginController
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-26 下午2:57:04
 * 修改人：yiyan
 * 修改时间：2014-11-26 下午2:57:04
 * 修改备注：
 * 
 * @version
 */
@Controller
public class LoginController
{
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login()
    {
        return "login";
    }

    @RequestMapping(value = "/relogin", method = RequestMethod.GET)
    public String relogin(Model model)
    {
        model.addAttribute("expired", "true");
        return "login";
    }

    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginerror(Model model)
    {
        model.addAttribute("error", "true");
        return "login";

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model)
    {
        return "login";
    }
}
