package com.youngo.admin.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.DHMDateFieldFormatter;

import java.util.Date;

/**
 * Created by 浮沉 on 2016/3/18.
 * 聊天消息，后台管理系统查看消息功能使用
 */
public class AdminChatInfo
{
    private static final long serialVersionUID = 4297561782619090340L;
    private String seq;
    private String content;
    @JsonIgnore
    private String id;
    private Date createtime;//消息的创建时间
    private String session_id;
    private AdminChatUser sender;//消息发送者
    private int type;

    public String getSeq()
    {
        return seq;
    }
    public void setSeq(String seq)
    {
        this.seq = seq;
    }
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }

    public AdminChatUser getSender()
    {
        return sender;
    }
    public void setSender(AdminChatUser sender)
    {
        this.sender = sender;
    }
    @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonSerialize(using = DHMDateFieldFormatter.class)
    public Date getCreatetime()
    {
        return createtime;
    }
    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }
    public String getSession_id()
    {
        return session_id;
    }
    public void setSession_id(String session_id)
    {
        this.session_id = session_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ChatInfo [content=" + content + ", sender=" + sender + ", createtime=" + createtime + "]";
    }
}
