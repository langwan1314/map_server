package com.youngo.core.service.msg;

import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.smapper.ChatListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 浮沉 on 2016/4/27.
 * 聊天会话列表的管理
 */
@Service
public class ChatListService
{
    @Autowired
    private ChatListMapper chatListMapper;
    /**
     * @param userId 用户ID
     * @return 用户的会话列表
     */
    public List<String> getSessionList(String userId)
    {
        return chatListMapper.getSessionList(userId);
    }

    /**
     * 更新消息发送方的会话列表
     * @param msg 消息
     */
    public void updateSenderSessionList(MessageEntity msg)
    {
        chatListMapper.updateSenderSessionList(msg);
    }

    /**
     * 更新未读消息列表
     * @param msg 消息
     */
    public void updateReceiverSessionList(MessageEntity msg)
    {
        chatListMapper.updateReceiverSessionList(msg);
    }

    public void removeSesseion(String userId , String peerId)
    {
        chatListMapper.removeSesseion(userId, peerId);
    }
}
