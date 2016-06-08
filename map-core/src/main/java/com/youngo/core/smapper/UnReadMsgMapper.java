package com.youngo.core.smapper;

import com.youngo.core.model.msg.ChatConstants;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.msg.SessionConfigure;
import com.youngo.core.model.msg.UnreadEntity;
import com.youngo.ssdb.core.*;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

/**
 * Created by 浮沉 on 2016/4/27.
 * 未读消息管理
 */
@Repository
public class UnReadMsgMapper
{
    private HashMapOperations<String, String, UnreadEntity> unReadOperations;//未读消息

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.unReadOperations = new DefaultHashMapOperations<>(ssdb, String.class, String.class, UnreadEntity.class);
    }

    /**
     * 更新未读消息列表
     */
    public void addUnReadMsgCount(MessageEntity msg)
    {
        int senderId = msg.getFromId();
        int receiverId = msg.getToId();
        addUnReadMsgCount(receiverId,senderId);
    }

    public void addUnReadMsgCount(int userId , int peerId)
    {
        String mapName = SsdbConstants.Chat.chatUnReadPrefix + userId;
        UnreadEntity entity = unReadOperations.get(mapName, String.valueOf(peerId));
        if (entity == null)
        {
            entity = new UnreadEntity();
            entity.setPeerId(peerId);
            entity.setSessionType(ChatConstants.SESSION_TYPE_SINGLE);//现在只有单人聊天
        }
        entity.setUnReadCnt(entity.getUnReadCnt() + 1);
        unReadOperations.set(mapName, String.valueOf(peerId), entity);//接收方新增一条未读消息
    }

    public UnreadEntity clearUnReadMsgCount(int userId, int peerId)
    {
        String mapName = SsdbConstants.Chat.chatUnReadPrefix + userId;
        UnreadEntity unreadEntity = unReadOperations.get(mapName, String.valueOf(peerId));
        unReadOperations.delete(mapName, String.valueOf(peerId));
        return unreadEntity;
    }

    public Collection<UnreadEntity> list(String userId)
    {
        String mapName = SsdbConstants.Chat.chatUnReadPrefix + userId;
        Map<String, UnreadEntity> allByName = unReadOperations.getAllByName(mapName);
        if (allByName == null)
        {
            return null;
        }
        return allByName.values();
    }

    public int getUnreadCount(String userId)
    {
        Collection<UnreadEntity> list = list(userId);
        if (list == null || list.isEmpty())
        {
            return 0;
        }
        int total = 0;
        for (UnreadEntity entity : list)
        {
            total = total + entity.getUnReadCnt();
        }
        return total;
    }
}
