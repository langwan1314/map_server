package com.youngo.admin.model.chat;

/**
 * Created by 浮沉 on 2016/3/18.
 * 聊天，用户信息，后台管理系统查看聊天消息中使用
 */
public class AdminChatUser
{
    private static final long serialVersionUID = 3984117521081939887L;
    private String id;
    private String icon;//用户头像的URL
    private String nick_name;
    private String user_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon()
    {
        return icon;
    }
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    public String getNick_name()
    {
        return nick_name;
    }
    public void setNick_name(String nick_name)
    {
        this.nick_name = nick_name;
    }
    public String getUser_name()
    {
        return user_name;
    }
    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }

    @Override
    public String toString()
    {
        return "ChatUser [icon=" + icon + ", nick_name=" + nick_name + ", user_name=" + user_name + "]";
    }
}
