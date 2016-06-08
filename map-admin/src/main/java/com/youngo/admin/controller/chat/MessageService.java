package com.youngo.admin.controller.chat;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.model.msg.EntityChangeEngine;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.msg.SessionEntity;
import com.youngo.ssdb.core.*;
import com.youngo.ssdb.core.entity.Tuple;
import org.nutz.ssdb4j.spi.SSDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by 浮沉 on 2016/3/16.
 * 消息服务
 */
@Service
public class MessageService
{
    private SortedSetOperations<String, String> setOperations;
    private ValueOperations<String, MessageEntity> valueOperations;
    private static final int MAX_MSG_COUNT = 100;

    protected static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.setOperations = new DefaultSortedSetOperations<>(ssdb, String.class, String.class);
        this.valueOperations = new DefaultValueOperations<>(ssdb, String.class, MessageEntity.class);
    }

    /**
     * @param userId 用户ID
     * @return 获取用户最近发送的N条消息<br>
     */
    public List<MessageEntity> getMessages(String userId)
    {
        List<MessageEntity> entitys = new LinkedList<>();
        List<String> sessionList = getSessionList(userId);
        if(sessionList!=null && !sessionList.isEmpty())
        {
            List<String> keys = new ArrayList<>();
            for(String sessionId : sessionList)
            {
                List<Tuple<String, Long>> tuples = setOperations.scanScoreReverse(sessionId, null, null, null, MAX_MSG_COUNT);
                if(tuples!=null && !tuples.isEmpty())
                {
                    for(Tuple<String,Long> tuple : tuples)
                    {
                        String key = tuple.getKey();
                        if(key.startsWith(SsdbConstants.Chat.chatDetailPrefix+userId+":"))//表示该消息是userId对应的用户发送的
                        {
                            keys.add(key);
                        }
                    }
                }
            }
            List<Tuple<String, MessageEntity>> temp = valueOperations.multiGet(keys);
            entitys = temp.stream().map(Tuple::getValue).collect(toList());
        }
        return entitys;
    }

    /**
     * @param userId 用户ID
     * @return 用户的会话列表（sessionId）
     */
    public List<String> getSessionList(String userId)
    {
        String sessionList = SsdbConstants.Chat.chatSessionListPrefix + userId;
        List<Tuple<String, Long>> tuples = setOperations.scanScoreReverse(sessionList, null, null, null, 100);//扫描出所有的sessionId
        return tuples.stream().map(Tuple::getKey).collect(toList());
    }

    /**
     * @param sessionId 聊天会话ID
     * @return sessionId对应的聊天话中，userId用户发送的最近N条消息
     */
    public List<MessageEntity> getMessagesBySessionId(String sessionId)
    {
        List<MessageEntity> entitys = new LinkedList<>();
        List<Tuple<String, Long>> tuples = setOperations.scanScoreReverse(sessionId, null, null, null, MAX_MSG_COUNT);
        if(tuples!=null && !tuples.isEmpty())
        {
            List<String> keys = new ArrayList<>();
            for(Tuple<String,Long> tuple : tuples)
            {
                String key = tuple.getKey();
                keys.add(key);
            }
            List<Tuple<String, MessageEntity>> temp = valueOperations.multiGet(keys);
            entitys = temp.stream().map(Tuple::getValue).collect(toList());
        }
        return entitys;
    }
}
