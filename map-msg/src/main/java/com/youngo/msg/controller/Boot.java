package com.youngo.msg.controller;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 浮沉 on 2016/3/22.
 */
public class Boot
{
    public static void main(String[] args) throws Exception
    {
        AbstractApplicationContext ctx  = new ClassPathXmlApplicationContext("/spring/applicationcontext.xml");
        Application.setContext(ctx);
        NettyServer nettyServer = ctx.getBean(NettyServer.class);
        nettyServer.start();
    }
}
