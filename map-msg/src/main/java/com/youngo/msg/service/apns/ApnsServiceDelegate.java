package com.youngo.msg.service.apns;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.youngo.core.mapper.user.UserDeviceMapper;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.user.UserDevice;
import com.youngo.core.service.msg.UnReadMsgService;
import com.youngo.msg.model.apns.ApnsConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 浮沉 on 2016/4/12.
 * 苹果消息推送服务
 */
@Service
public class ApnsServiceDelegate implements InitializingBean
{
    private static final Logger logger = LoggerFactory.getLogger(ApnsServiceDelegate.class);
    @Autowired
    private ApnsConfigure apnsConfigure;
    private ApnsService service;
    @Autowired
    private UserDeviceMapper userPushDeviceMapper;
    @Autowired
    private UnReadMsgService unReadMsgService;

    public void push(int serviceId, int commandId, Object body, String userId)
    {
        UserDevice deviceInfo = getDeviceInfo(userId);
        if (deviceInfo != null)
        {
            String token = deviceInfo.getRegistrationId();
            String language = deviceInfo.getLanguage();
            if (!StringUtils.isEmpty(token))
            {
                String message = getMessage(language);
                int unreadCount = unReadMsgService.getUnreadCount(userId);
                String payload = APNS.newPayload().alertTitle("bello").alertBody(message).sound("default").badge(unreadCount).customField("serviceId", serviceId)
                        .customField("commandId", commandId)
                        .customField("body", body).build();
                service.push(token, payload);
                feedBack();
            }
        }
    }

    private UserDevice getDeviceInfo(String userId)
    {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("loginStatus", 1);//1表示登录状态
        param.put("deviceSource", "ios");
        return userPushDeviceMapper.getDetailInfo(param);
    }

    private String getMessage(String local)
    {
        if ("zh-CN".equals(local))
        {
            return "您有一条新消息";
        } else
        {
            return "You have a message";
        }
    }

    private void feedBack()
    {
        Map<String, Date> inactiveDevices = service.getInactiveDevices();
        for (String deviceToken : inactiveDevices.keySet())
        {
            Date date = inactiveDevices.get(deviceToken);
            System.out.println("token : " + deviceToken + " ,date : " + date);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        try
        {
            InputStream stream = ApnsServiceDelegate.class.getClassLoader().getResourceAsStream(apnsConfigure.getCertPath());
            service = APNS.newService().withCert(stream, apnsConfigure.getPassword()).withSandboxDestination().build();
        } catch (Exception e)
        {
            logger.error("apns证书加载失败", e);
        }
    }
}
