package com.youngo.core.model.msg;

import org.springframework.util.StringUtils;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 */
public class EntityChangeEngine
{

    public static SessionEntity getSessionEntity(MessageEntity msg)
    {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId((long)msg.getId());
        // [图文消息] [图片] [语音]
        sessionEntity.setLatestMsgData(msg.getMessageDisplay());
        sessionEntity.setUpdated(msg.getUpdated());
        sessionEntity.setCreated(msg.getUpdated());
        sessionEntity.setLatestMsgId(msg.getMsgId());
        //sessionEntity.setPeerId(msg.getFromId());
        sessionEntity.setTalkId(msg.getFromId());
        sessionEntity.setPeerType(msg.getSessionType());
        sessionEntity.setLatestMsgType(msg.getMsgType());
        return  sessionEntity;
    }

    public static String getSessionKey(int peerId,int sessionType)
    {
        String sessionKey = sessionType + "_" + peerId;
        return sessionKey;
    }

    public static String[] spiltSessionKey(String sessionKey)
    {
        if(StringUtils.isEmpty(sessionKey)){
            throw new IllegalArgumentException("spiltSessionKey error,cause by empty sessionKey");
        }
        String[] sessionInfo = sessionKey.split("_",2);
        return sessionInfo;
    }

}
