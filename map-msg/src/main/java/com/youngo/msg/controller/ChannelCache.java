package com.youngo.msg.controller;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fuchen on 2015/12/22.
 * ChannelHandlerContext缓存，消息推送的时候需要用到<br>
 * 后续还需要在SSDB上维持一份记录，以便消息服务器做分布式的时候寻路用
 */
public class ChannelCache
{
    private Map<String, Channel> cache = new ConcurrentHashMap<>();

    private ChannelCache()
    {
    }

    private static ChannelCache instance = new ChannelCache();

    public static ChannelCache getInstance()
    {
        return instance;
    }

    public void put(String key, Channel ctx)
    {
        cache.put(key, ctx);
    }

    public Channel get(String key)
    {
        return cache.get(key);
    }

//    public void remove(String key)
//    {
//        cache.remove(key);
//    }

    public void remove(String key , Channel value)
    {
        cache.remove(key,value);
    }

    public void clear()
    {
        cache.clear();
    }

    public int size()
    {
        return cache.size();
    }
}
