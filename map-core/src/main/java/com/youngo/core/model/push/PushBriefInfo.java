package com.youngo.core.model.push;

/**
 * Created by 浮沉 on 2016/5/11.<br>
 * 待推送信息的摘要
 */
public class PushBriefInfo
{
    private int serviceId;
    private int commandId;
    private String msgId;

    public int getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(int serviceId)
    {
        this.serviceId = serviceId;
    }

    public int getCommandId()
    {
        return commandId;
    }

    public void setCommandId(int commandId)
    {
        this.commandId = commandId;
    }

    public String getMsgId()
    {
        return msgId;
    }

    public void setMsgId(String msgId)
    {
        this.msgId = msgId;
    }
}
