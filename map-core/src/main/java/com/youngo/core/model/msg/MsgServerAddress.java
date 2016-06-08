package com.youngo.core.model.msg;

/**
 * Created by fuchen on 2015/12/18.<br>
 * 消息服务器地址
 */
public class MsgServerAddress
{
    private String priorIP;
    private String backupIP;
    private int port;//服务器端口

    public String getPriorIP()
    {
        return priorIP;
    }

    public void setPriorIP(String priorIP)
    {
        this.priorIP = priorIP;
    }

    public String getBackupIP()
    {
        return backupIP;
    }

    public void setBackupIP(String backupIP)
    {
        this.backupIP = backupIP;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }
}
