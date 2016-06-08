package com.youngo.msg.model;

import com.youngo.msg.controller.ChannelCache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by fuchen on 2016/2/16.
 * 消息服务器状态
 */
public class MsgServerInfo
{
    private static MsgServerInfo ins = new MsgServerInfo();
    private AtomicLong msgCount = new AtomicLong();//客户端消息数统计

    private MsgServerInfo ()
    {
    }

    public static MsgServerInfo getInstance()
    {
        return ins;
    }

    /**
     * @return 客户端连接数
     */
    public int getClientCount()
    {
        return ChannelCache.getInstance().size();
    }

    /**
     * 消息数加1
     */
    public void addMsgCount()
    {
        msgCount.incrementAndGet();
    }

    /**
     * @return 获取消息数目
     */
    public long getMsgCount()
    {
        return msgCount.get();
    }

    public void clearMsgCount()
    {
        msgCount.set(0);
    }


}
