package com.youngo.mobile.model.friendship;

import com.youngo.core.model.msg.SessionConfigure;
import com.youngo.core.model.user.UserBrief;

/**
 * Created by 浮沉 on 2016/4/8.
 *
 */
public class FriendInfo extends UserBrief
{
    private boolean temp;//true表示正式好友，false表示临时好友
    private SessionConfigure sessionConfig;

    public boolean isTemp()
    {
        return temp;
    }

    public void setTemp(boolean temp)
    {
        this.temp = temp;
    }

    public SessionConfigure getSessionConfig()
    {
        return sessionConfig;
    }

    public void setSessionConfig(SessionConfigure sessionConfig)
    {
        this.sessionConfig = sessionConfig;
    }
}
