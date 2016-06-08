package com.youngo.core.service.msg;

import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.msg.UnreadEntity;
import com.youngo.core.smapper.UnReadMsgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by 浮沉 on 2016/4/27.
 */
@Service
public class UnReadMsgService
{
    @Autowired
    private UnReadMsgMapper unReadMsgMapper;
    /**
     * 更新未读消息列表
     */
    public void addUnReadMsgCount(MessageEntity msg)
    {
        unReadMsgMapper.addUnReadMsgCount(msg);
    }

    public UnreadEntity clearUnReadMsgCount(int userId,int peerId)
    {
        return unReadMsgMapper.clearUnReadMsgCount(userId, peerId);
    }

    public Collection<UnreadEntity> list(String userId)
    {
        return unReadMsgMapper.list(userId);
    }

    public int getUnreadCount(String userId)
    {
        return unReadMsgMapper.getUnreadCount(userId);
    }
}
