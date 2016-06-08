package com.youngo.core.smapper;

import com.youngo.core.model.msg.MessageEntity;
import com.youngo.ssdb.core.*;
import com.youngo.ssdb.core.entity.Tuple;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Created by fuchen on 2016/1/9.
 * 聊天消息，保存，获取
 */
@Repository
public class ChatMapper extends ChatBaseMapper
{
    private SortedSetOperations<String, String> setOperations;//会话
    private ValueOperations<String, MessageEntity> valueOperations;//聊天消息的存储
    private HashMapOperations<String, String, Integer> msgCountOperations;//会话消息计数器，用来辅助生成消息ID

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.setOperations = new DefaultSortedSetOperations<>(ssdb, String.class, String.class);
        this.valueOperations = new DefaultValueOperations<>(ssdb, String.class, MessageEntity.class);
        this.msgCountOperations = new DefaultHashMapOperations<>(ssdb, String.class, String.class, Integer.class);
    }

    /**
     * 将消息写入kv
     *
     * @param msg 消息
     */
    public void saveKV(MessageEntity msg)
    {
        int senderId = msg.getFromId();
        int receiverId = msg.getToId();
        String msgKey = SsdbConstants.Chat.chatDetailPrefix + senderId + ":" + receiverId + ":" + msg.getId();
        valueOperations.set(msgKey, msg);
    }

    /**
     * 更新消息发送方的会话
     *
     * @param msg 消息
     */
    public void updateSenderSession(MessageEntity msg)
    {
        String senderSessionName = getSenderSessionName(msg);
        String msgKey = getMessageKey(msg);
        setOperations.set(senderSessionName, msgKey, msg.getId());
    }

    /**
     * 更新消息接收方的会话
     *
     * @param msg 消息
     */
    public void updateReceiverSession(MessageEntity msg)
    {
        String receiverSessionName = getReceiverSessionName(msg);
        String msgKey = getMessageKey(msg);
        setOperations.set(receiverSessionName, msgKey, msg.getId());
    }

    public Long clearSession(String sessionId)
    {
        return setOperations.clear(sessionId);//清空该会话
    }

    /**
     * 生成自增长的消息ID
     */
    public void buildMessageId(MessageEntity msg)
    {
        String senderSessionName = getSenderSessionName(msg);
        String receiverSessionName = getReceiverSessionName(msg);
        String sessionName = senderSessionName.compareTo(receiverSessionName) < 0 ? senderSessionName : receiverSessionName;
        int id = msgCountOperations.increment(SsdbConstants.Chat.chatCountPrefix, sessionName, 1);//获取消息队列的大小
        msg.setId(id);
    }

    /*
     * 获取消息在SSDB KV存储中的key
     */
    private String getMessageKey(MessageEntity msg)
    {
        int senderId = msg.getFromId();
        int receiverId = msg.getToId();
        return SsdbConstants.Chat.chatDetailPrefix + senderId + ":" + receiverId + ":" + msg.getId();
    }

    public MessageEntity getMessageEntity(int userId, int peerId, int index)
    {
        String key1 = userId + ":" + peerId;
        MessageEntity message = valueOperations.get(SsdbConstants.Chat.chatDetailPrefix + key1 + ":" + index);
        if (message != null)
        {
            return message;
        }
        String key2 = peerId + ":" + userId;
        return valueOperations.get(SsdbConstants.Chat.chatDetailPrefix + key2 + ":" + index);
    }

    public String getLastMessageId(String sessionId)
    {
        List<Tuple<String, Long>> detailTuple = setOperations.scanScoreReverse(sessionId, null, null, null, 1);
        if (detailTuple != null && !detailTuple.isEmpty())
        {
            return detailTuple.get(0).getKey();
        }
        return null;
    }

    public MessageEntity getMessageEntity(String key)
    {
        return valueOperations.get(key);
    }

    public List<MessageEntity> getMessageEntitys(List<String> keys)
    {
        List<Tuple<String, MessageEntity>> tuples = valueOperations.multiGet(keys);
        return tuples.stream().map(Tuple::getValue).collect(toList());
    }

    /**
     * 获取某个用户与另外的用户的会话信息。
     *
     * @param userId
     * @param keys
     * @return
     */
    public Map<String, Long> getUserChatSessionByUserIds(String userId, Set<String> keys)
    {
        Map<String, Long> chated = setOperations.multiGetAsMap(SsdbConstants.Chat.chatSessionListPrefix + userId, keys);
        return chated;
    }

}
