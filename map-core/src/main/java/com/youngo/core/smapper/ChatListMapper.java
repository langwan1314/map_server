package com.youngo.core.smapper;

import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.msg.SessionConfigure;
import com.youngo.core.model.msg.UnreadEntity;
import com.youngo.ssdb.core.*;
import com.youngo.ssdb.core.entity.Tuple;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by 浮沉 on 2016/4/27.
 * 聊天会话列表，ssdb访问
 */
@Repository
public class ChatListMapper extends ChatBaseMapper
{
    private SortedSetOperations<String, String> setOperations;//会话列表，会话

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.setOperations = new DefaultSortedSetOperations<>(ssdb, String.class, String.class);
    }

    /**
     * @param userId 用户ID
     * @return 用户的会话列表
     */
    public List<String> getSessionList(String userId)
    {
        String sessionList = SsdbConstants.Chat.chatSessionListPrefix + userId;
        List<Tuple<String, Long>> tuples = setOperations.scanScoreReverse(sessionList, null, null, null, 100);//扫描出所有的sessionId
        return tuples.stream().map(Tuple::getKey).collect(toList());
    }

    /**
     * 更新消息发送方的会话列表
     * @param msg 消息
     */
    public void updateSenderSessionList(MessageEntity msg)
    {
        String senderSessionName = getSenderSessionName(msg);;
        String sendSessionListName = getSenderSessionListName(msg);
        setOperations.set(sendSessionListName, senderSessionName, msg.getCreated());
    }

    /**
     * 更新未读消息列表
     * @param msg 消息
     */
    public void updateReceiverSessionList(MessageEntity msg)
    {
        String receiverSessionName = getReceiverSessionName(msg);
        String receiverSessionListName = getReceiverSessionListName(msg);
        setOperations.set(receiverSessionListName, receiverSessionName, msg.getCreated());
    }

    public void removeSesseion(String userId , String peerId)
    {
        String sessionName = getSessionName(userId, peerId, 1);
        setOperations.delete(SsdbConstants.Chat.chatSessionListPrefix + userId,sessionName);//将该会话从会话列表中删除
    }
}
