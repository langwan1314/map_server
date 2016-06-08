package com.youngo.core.common;

import javax.servlet.http.HttpServletRequest;

import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import com.youngo.ssdb.core.StringValueOperations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.youngo.core.common.CommonStaticConstant.*;

/**
 * @author fuchen 上下文，用来获取Request ， Response等
 */
public class Context
{
    private static final Logger logger = LoggerFactory.getLogger(Context.class);
    private static ApplicationContext applicationContext;
    
    public static HttpServletRequest getRequest()
    {
        RequestAttributes attr = RequestContextHolder.getRequestAttributes();
        if (attr instanceof ServletRequestAttributes)
        {
            return ((ServletRequestAttributes) attr).getRequest();
        } else
        {
            logger.error("获取Request请求失败：" + attr.getClass().getName());
        }
        return null;
    }
    
    public static String getUserId()
    {
        HttpServletRequest request = getRequest();
        if(request==null)
        {
           return null;
        }
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        return userId;
    }
    
    /**
     * 获取userId，如果userId为空，抛出异常，提示客户端重新登录
     * @return 用户ID
     */
    public static String forceGetUserId()
    {
        HttpServletRequest request = getRequest();
        if(request==null)
        {
            throw new RestException(Result.param_error,Result.empty_param_msg);
        }
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        if(userId == null){
        	String passport = request.getHeader("passport");
        	if(null != passport && passport.length() == 32){
        		StringValueOperations operation = (StringValueOperations) getBean("valueOperations");
        		userId = operation.get(passport);
        	}
        }
        if(userId == null){
        	throw new RestException(Result.session_expired,Result.session_expired_msg);
        }
        
        return userId;
    }
    
    /**
     * @return 从head中获取客户端版本信息
     */
    public static int getClientVersion()
    {
        HttpServletRequest request = getRequest();
        if (request != null)
        {
            String clientVersion = request.getHeader(CLIENT_VERSION);
            clientVersion = StringUtils.isEmpty(clientVersion) ? "0" : clientVersion;
            return Integer.valueOf(clientVersion.replaceAll("[.]", "0"));
        }
        return 0;
    }
    
    /**
     * 获取spring中的bean信息
     * @param name
     * @return
     */
	public static Object getBean(String name)
	{
		return getApplicationContext().getBean(name);
	}
    
    static ApplicationContext getApplicationContext() {
    	if(applicationContext == null){
    		synchronized (Context.class) 
    		{
    			while (applicationContext == null) 
    			{
    				try 
    				{
    					Context.class.wait(6000);
    					if (applicationContext == null) 
    					{
    					}
    				} catch (InterruptedException ex) 
    				{
    				}
    			}
    			return applicationContext;
    		}
    	}else{
    		return applicationContext;
    	}
	}
    
    static void setApplicationContext(ApplicationContext applicationContext) {
    	synchronized (Context.class)
    	{
    		Context.applicationContext = applicationContext;
    		Context.class.notifyAll();
    	}
    }
}
