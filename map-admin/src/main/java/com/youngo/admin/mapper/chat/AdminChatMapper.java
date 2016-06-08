package com.youngo.admin.mapper.chat;

import com.youngo.admin.model.chat.AdminChatUser;

/**
 * Created by 浮沉 on 2016/3/18.
 * 聊天，获取聊天的用户基本信息
 */
public interface AdminChatMapper
{
    AdminChatUser getChatUser(String id);
}
