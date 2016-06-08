package com.youngo.core.listener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.model.ForceLogin;
import com.youngo.core.model.Login;
import com.youngo.core.smapper.AuthMapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * 拦截用户是否登录 未登陆的需要返回提示
 * @author atao 2014-10-16
 */
public class LoginAnnotationInterceptor extends HandlerInterceptorAdapter
{
	public LoginAnnotationInterceptor(AuthMapper authMapper){
		this.authMapper = authMapper;
//		this.valueOperations = valueOperations;
	}
	
//	private StringValueOperations valueOperations;
	
	private AuthMapper authMapper;
	
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handler2 = (HandlerMethod) handler;
            Login login = handler2.getMethodAnnotation(Login.class);
            if (null != login)
            {
            	String passport = request.getHeader("passport");
            	if(null != passport && passport.length() == 36){
//            		StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_PASSPORT_PREFIX).append(passport);
//            		String userId = valueOperations.get(sb.toString());
            		String userId = authMapper.getPassportUserId(passport);
            		if(null != userId && userId.length() > 0){
            			request.setAttribute(CommonStaticConstant.USER_ID, userId);
            			return true;
            		}
            	}
            	if(login.forceLogin().equals(ForceLogin.no)){
            		//不强制登陆，仅仅获取用户名
            		return true;
            	}
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=UTF-8");
                OutputStream out = response.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
                pw.println("{\"code\":\"" + HttpStatus.NOT_ACCEPTABLE.value() + "\",\"msg\":\"请先登录\"}");
                pw.flush();
                pw.close();
                return false;
            }
        }
        return true;
    }
    
/*    private String getPassport(HttpServletRequest request)
    {
        return null;
    }
*/
}
