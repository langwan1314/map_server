package com.youngo.core.smapper;

import com.youngo.core.model.msg.MessageEntity;
import com.youngo.ssdb.core.SsdbConstants;


/**
 * Created by 浮沉 on 2016/4/27.
 */
public class ChatBaseMapper
{
    protected String getSenderSessionName(MessageEntity msg)
    {
        int senderId = msg.getFromId();
        int receiverId = msg.getToId();
        return getSessionName(senderId,receiverId,1);
    }

    protected String getReceiverSessionName(MessageEntity msg)
    {
        int senderId = msg.getFromId();
        int receiverId = msg.getToId();
        return getSessionName(receiverId,senderId,1);
    }

    /**
     * @param userId 用户id
     * @param peerId 聊天伙伴的ID
     * @param peerType 伙伴类型(好友，群，讨论组）
     * @return 会话的唯一标识，因为现在没有群聊，peerType字段暂时忽略
     */
    protected String getSessionName(int userId , int peerId , int peerType)
    {
        return SsdbConstants.Chat.chatSessionPrefix + userId + ":" + peerId;
    }

    /**
     * @param userId 用户id
     * @param peerId 聊天伙伴的ID
     * @param peerType 伙伴类型(好友，群，讨论组）
     * @return 会话的唯一标识，因为现在没有群聊，peerType字段暂时忽略
     */
    public String getSessionName(String userId , String peerId , int peerType)
    {
        return SsdbConstants.Chat.chatSessionPrefix + userId + ":" + peerId;
    }

    protected String getSenderSessionListName(MessageEntity msg)
    {
        return SsdbConstants.Chat.chatSessionListPrefix + msg.getFromId();
    }

    protected String getReceiverSessionListName(MessageEntity msg)
    {
        return SsdbConstants.Chat.chatSessionListPrefix + msg.getToId();
    }
}
