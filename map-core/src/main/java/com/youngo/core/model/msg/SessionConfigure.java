package com.youngo.core.model.msg;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by 浮沉 on 2016/4/25.
 * 聊天会话设置
 */
public class SessionConfigure
{
    private int peerId=-1;
    private int peerType=-1;
    private boolean top; //true表示置顶，false表示普通会话
    private boolean notice = true; //true表示接收消息，false表示屏蔽消息
    private String stranslate; //发送的语言翻译成什么语言
    private String rtranslate; //我收到的语言翻译成什么语言

    public int getPeerId()
    {
        return peerId;
    }

    public void setPeerId(int peerId)
    {
        this.peerId = peerId;
    }

    public int getPeerType()
    {
        return peerType;
    }

    public void setPeerType(int peerType)
    {
        this.peerType = peerType;
    }

    public boolean isTop()
    {
        return top;
    }

    public void setTop(boolean top)
    {
        this.top = top;
    }

    public boolean isNotice()
    {
        return notice;
    }

    public void setNotice(boolean notice)
    {
        this.notice = notice;
    }

    public String getStranslate()
    {
        return stranslate;
    }

    public void setStranslate(String stranslate)
    {
        this.stranslate = stranslate;
    }

    public String getRtranslate()
    {
        return rtranslate;
    }

    public void setRtranslate(String rtranslate)
    {
        this.rtranslate = rtranslate;
    }
}
