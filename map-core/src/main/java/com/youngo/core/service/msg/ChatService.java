package com.youngo.core.service.msg;

import com.youngo.core.common.Base64;
import com.youngo.core.mapper.friendship.BlackListMapper;
import com.youngo.core.model.friendship.BlackListItem;
import com.youngo.core.model.msg.*;
import com.youngo.core.smapper.ChatListMapper;
import com.youngo.core.smapper.ChatMapper;
import com.youngo.core.smapper.UnReadMsgMapper;
import com.youngo.ssdb.core.SsdbConstants;
import com.youngo.ssdb.core.entity.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 浮沉 on 2016/4/11.
 * 聊天消息处理
 */
@Service
public class ChatService
{
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private ChatListMapper chatListMapper;
    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private UnReadMsgMapper unReadMsgMapper;

    public MessageEntity createTextMessage(int fromId,int toId,String content)
    {
        MessageEntity entity = new MessageEntity();
        entity.setFromId(fromId);
        entity.setToId(toId);
        entity.setContent(content);
        int current = (int)(System.currentTimeMillis()/1000);
        entity.setCreated(current);
        entity.setMsgType(ChatConstants.MSG_TYPE_SINGLE_TEXT);
        entity.setUpdated(current);
        entity.setDisplayType(MsgAnalyzeEngine.getDisPlayType(entity));
        entity.setStatus(3);
        return entity;
    }

    public MessageEntity createVoiceMessage(int fromId , int toId , byte[] content)
    {
        MessageEntity entity = new MessageEntity();
        entity.setFromId(fromId);
        entity.setToId(toId);
        String encode = Base64.encode(content);
        entity.setContent(encode);
        int current = (int)(System.currentTimeMillis()/1000);
        entity.setCreated(current);
        entity.setMsgType(ChatConstants.MSG_TYPE_SINGLE_AUDIO);
        entity.setUpdated(current);
        entity.setDisplayType(MsgAnalyzeEngine.getDisPlayType(entity));
        entity.setStatus(3);
        return entity;
    }

    public byte[] intToBytes(int n)
    {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    /**
     * 保存聊天消息
     * @param msg 待保存的聊天消息
     */
    public void save(MessageEntity msg)
    {
        //初始化msg的ID
        chatMapper.buildMessageId(msg);
        //先将消息写入到KV中
        chatMapper.saveKV(msg);
        //然后更新消息发送者的会话
        chatMapper.updateSenderSession(msg);
        //然后更新消息发送者的会话列表（根据最后一条消息的时间重排序）
        chatListMapper.updateSenderSessionList(msg);

        if(validate(msg))
        {
            //更新消息接收方的会话
            chatMapper.updateReceiverSession(msg);
            //更新接收方的会话列表
            chatListMapper.updateReceiverSessionList(msg);
            //更新未读消息列表（接收方）
            unReadMsgMapper.addUnReadMsgCount(msg);
        }
    }

    public void saveBuddyAgreeMessage(MessageEntity msg)
    {
        //初始化msg的ID
        chatMapper.buildMessageId(msg);
        //先将消息写入到KV中
        chatMapper.saveKV(msg);
        if(validate(msg))
        {
            //更新接收方的会话列表
            chatListMapper.updateReceiverSessionList(msg);
            //更新消息接收方的会话
            chatMapper.updateReceiverSession(msg);
            //更新未读消息列表（接收方）
            unReadMsgMapper.addUnReadMsgCount(msg);
        };
    }

    /**
     * @param msg 纸飞机消息携带到聊天会话中<br>
     *            不需要更新sessionList<br>
     *            不需要更新未读消息列表<br>
     */
    public void savePaperPlaneMessage(MessageEntity msg)
    {
        //初始化msg的ID
        chatMapper.buildMessageId(msg);
        //先将消息写入到KV中
        chatMapper.saveKV(msg);
        //然后更新消息发送者的会话
        chatMapper.updateSenderSession(msg);
        if(validate(msg))
        {
            //更新消息接收方的会话
            chatMapper.updateReceiverSession(msg);
            //更新未读消息列表（接收方）
            unReadMsgMapper.addUnReadMsgCount(msg.getFromId(),msg.getToId());
        };
    }

    private boolean validate(MessageEntity msg)
    {
        int senderId = msg.getFromId();
        int receiverId = msg.getToId();
        Map<String,String> params = new HashMap<>();
        params.put("userId",String.valueOf(receiverId));
        params.put("blackId",String.valueOf(senderId));
        List<BlackListItem> list = blackListMapper.list(params);
        return list==null || list.isEmpty();
    }

    public MessageEntity getMessageEntity(int userId, int peerId, int index)
    {
        return chatMapper.getMessageEntity(userId,peerId,index);
    }

    public MessageEntity getMessageEntity(String key)
    {
        return chatMapper.getMessageEntity(key);
    }

    public List<MessageEntity> getMessageEntitys(List<String> keys)
    {
        return chatMapper.getMessageEntitys(keys);
    }

    public MessageEntity getLastMessage(String sessionId)
    {
        String lastMessageId = chatMapper.getLastMessageId(sessionId);
        if(!StringUtils.isEmpty(lastMessageId))
        {
            return getMessageEntity(lastMessageId);
        }
        return null;
    }

    /**
     * @param userId 用户id
     * @param peerId 聊天伙伴的ID
     * @param peerType 伙伴类型(好友，群，讨论组）
     * @return 会话的唯一标识，因为现在没有群聊，peerType字段暂时忽略
     */
    public String getSessionName(String userId , String peerId , int peerType)
    {
        return chatMapper.getSessionName(userId,peerId,peerType);
    }

    /**
     * 清空会话
     * @param sessionId 会话id
     * @return 被删除的消息数目
     */
    public Long clearSession(String sessionId)
    {
        return chatMapper.clearSession(sessionId);
    }

    public Map<String, Long> getUserChatSessionByUserIds(String userId, Set<String> keys){
    	return chatMapper.getUserChatSessionByUserIds(userId, keys);
    }
}
