package com.youngo.netty;

import io.netty.channel.Channel;

/**
 * Created by 浮沉 on 2016/3/23.
 * netty上下文
 */
public class NettyContext
{
    private static Channel channel;

    public static Channel getChannel() {
        return channel;
    }

    public static void setChannel(Channel channel) {
        NettyContext.channel = channel;
    }
}
