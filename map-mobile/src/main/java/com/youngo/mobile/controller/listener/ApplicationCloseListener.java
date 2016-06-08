package com.youngo.mobile.controller.listener;

import com.youngo.mobile.controller.push.NettyClient;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * Created by fuchen on 2016/1/27.
 */
public class ApplicationCloseListener implements ApplicationListener
{
    @Override
    public void onApplicationEvent(ApplicationEvent event)
    {
        if(event instanceof ContextClosedEvent)
        {
            NettyClient.shutdown();
        }
    }
}
