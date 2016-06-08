package com.youngo.msg.controller.handler;

import com.youngo.core.common.DaemonThreadFactory;
import com.youngo.msg.controller.ChannelCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by fuchen on 2016/2/15.<br>
 * 自动将闲置时间过长的客户端断开
 */
public class IdleDisConnectHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(BuddyHandler.class);
    private static final long readerIdleTimeNanos = TimeUnit.SECONDS.toNanos(600);

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,new DaemonThreadFactory());

    public static Map<String,CacheValue> channelCache = Collections.synchronizedMap(new LinkedHashMap<>(10000, 0.75f, true));

    static
    {
        executorService.scheduleWithFixedDelay(new ReaderIdleTimeoutTask(),30,400, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        String channelId = ctx.channel().attr(AuthHandler.userIdKey).get();
        if (!StringUtils.isEmpty(channelId))
        {
            channelCache.remove(channelId);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        String channelId = ctx.channel().attr(AuthHandler.userIdKey).get();
        if(!StringUtils.isEmpty(channelId))
        {
            CacheValue value = channelCache.get(channelId);
            if (value == null) {
                value = new CacheValue();
                value.setLastReadTime(System.nanoTime());
                value.setChannel(ctx.channel());
                channelCache.put(channelId, value);
            }else
            {
                value.setLastReadTime(System.nanoTime());
            }
        }
        ctx.fireChannelRead(msg);
    }

    private static class ReaderIdleTimeoutTask implements Runnable
    {
        @Override
        public void run()
        {
            Iterator<Map.Entry<String, CacheValue>> iterator = channelCache.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<String, CacheValue> next = iterator.next();
                String key = next.getKey();
                CacheValue value = next.getValue();
                Channel channel = value.getChannel();
                long lastReadTime = value.getLastReadTime();
                // Reader is idle - set a new timeout and notify the callback.
                long nextDelay = readerIdleTimeNanos - (System.nanoTime() - lastReadTime);
                if (nextDelay <= 0)
                {
                    logger.info("close IdleConnection : "+key);
                    ChannelCache.getInstance().remove(key,channel);
                    channel.close();
                    iterator.remove();
                }
            }
        }
    }

    private class CacheValue
    {
        private Channel channel;
        private long lastReadTime;

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public long getLastReadTime() {
            return lastReadTime;
        }

        public void setLastReadTime(long lastReadTime) {
            this.lastReadTime = lastReadTime;
        }
    }

}
