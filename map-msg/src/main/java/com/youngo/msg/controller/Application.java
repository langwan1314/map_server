package com.youngo.msg.controller;

import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by 浮沉 on 2016/3/23.
 */
public class Application
{
    private static AbstractApplicationContext context;

    public static AbstractApplicationContext getContext() {
        return context;
    }

    public static void setContext(AbstractApplicationContext context) {
        Application.context = context;
    }
}
