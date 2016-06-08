package com.youngo.core.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.JasonDateFieldFormatter;

import java.util.Date;

/**
 * @Title: UserDevice.java
 * @Description: 用户的移动设备信息
 */
public class UserDevice
{
    private String id;
    private String userId;//用户id
    private String imei;//设备唯一标识
    private String registrationId;//极光推送用户标识
    private String deviceSource;//设备源，ios|android
    private String loginStatus;//登录状态， 0代表登出，1代表登录
    private String language;//客户端的语言
    private String screenSize;//屏幕尺寸，如500,600
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = JasonDateFieldFormatter.class)
    private Date createtime;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = JasonDateFieldFormatter.class)
    private Date lastupdatetime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getImei()
    {
        return imei;
    }

    public void setImei(String imei)
    {
        this.imei = imei;
    }

    public String getRegistrationId()
    {
        return registrationId;
    }

    public void setRegistrationId(String registrationId)
    {
        this.registrationId = registrationId;
    }

    public String getDeviceSource()
    {
        return deviceSource;
    }

    public void setDeviceSource(String deviceSource)
    {
        this.deviceSource = deviceSource;
    }

    public String getLoginStatus()
    {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus)
    {
        this.loginStatus = loginStatus;
    }

    public Date getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }

    public Date getLastupdatetime()
    {
        return lastupdatetime;
    }

    public void setLastupdatetime(Date lastupdatetime)
    {
        this.lastupdatetime = lastupdatetime;
    }

    public String getScreenSize()
    {
        return screenSize;
    }

    public void setScreenSize(String screenSize)
    {
        this.screenSize = screenSize;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }
}
