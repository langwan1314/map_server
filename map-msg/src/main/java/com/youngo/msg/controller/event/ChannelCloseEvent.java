package com.youngo.msg.controller.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by 浮沉 on 2016/4/27.<br>
 * 长连接通道关闭事件
 */
public class ChannelCloseEvent extends ApplicationEvent
{
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ChannelCloseEvent(String source)
    {
        super(source);
    }

    @Override
    public String getSource()
    {
        return (String)super.getSource();
    }
}
