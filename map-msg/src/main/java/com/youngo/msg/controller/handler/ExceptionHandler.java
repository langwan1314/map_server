package com.youngo.msg.controller.handler;

import com.youngo.msg.controller.Application;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.controller.event.ChannelCloseEvent;
import com.youngo.msg.controller.event.EventPublisher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by fuchen on 2015/12/18.
 * 异常处理
 */
public class ExceptionHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    private static EventPublisher eventPublisher = Application.getContext().getBean(EventPublisher.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        String userId = ctx.channel().attr(AuthHandler.userIdKey).get();
        if(userId!=null)
        {
            ChannelCache.getInstance().remove(userId,ctx.channel());
            ChannelCloseEvent event = new ChannelCloseEvent(userId);
            eventPublisher.publish(event);
        }
        logger.error("channel inactive"+userId);
        ctx.channel().close();
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error("netty 通信异常", cause);
        String userId = ctx.channel().attr(AuthHandler.userIdKey).get();
        if(userId!=null)
        {
            ChannelCache.getInstance().remove(userId,ctx.channel());
        }
//        ctx.close();
    }
}
