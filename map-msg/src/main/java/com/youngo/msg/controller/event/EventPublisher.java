/**
 * 事件发布器，异步发布
 */
package com.youngo.msg.controller.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *事件发布
 */
@Component
public class EventPublisher implements ApplicationContextAware
{
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private ApplicationContext ctx;

    /*
     * (non-Javadoc)
     * <p>Title: setApplicationContext</p>
     * <p>Description: </p>
     * @param applicationContext
     * @throws BeansException
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext
     * (org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.ctx = applicationContext;
    }

    /**
     * 发布事件
     * @param event 待发布的事件
     */
    public void publish(ApplicationEvent event)
    {
        executorService.execute(() -> {ctx.publishEvent(event);});
    }

}
