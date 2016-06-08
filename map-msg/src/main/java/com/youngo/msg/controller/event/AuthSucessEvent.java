package com.youngo.msg.controller.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by 浮沉 on 2016/5/11.<br?
 * 认证成功事件
 */
public class AuthSucessEvent extends ApplicationEvent
{
    public AuthSucessEvent(String source)
    {
        super(source);
    }

    @Override
    public String getSource() {
        return (String)super.getSource();
    }
}
