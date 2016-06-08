package com.youngo.msg.util;

import com.google.protobuf.ByteString;
import com.youngo.core.common.Base64;
import com.youngo.core.model.msg.ChatConstants;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.protobuf.IMMessage;
import com.youngo.core.service.msg.MsgAnalyzeEngine;

/**
 * Created by fuchen on 2016/1/18.<br>
 * protobuf到实体之间的转换
 */
public class PB2Entity
{
    public static MessageEntity getMessageEntity(IMMessage.IMMsgData msgData)
    {
        int fromUserId = msgData.getFromUserId();
        int toUserId = msgData.getToUserId();
        ByteString data = msgData.getMsgData();
        MessageEntity msg = new MessageEntity();
        msg.setFromId(fromUserId);
        msg.setToId(toUserId);
        msg.setCreated((int)(System.currentTimeMillis()/1000));
        msg.setUpdated(msg.getCreated());
        msg.setStatus(3);
        int msgType = MsgAnalyzeEngine.getMsgType(msgData);
        if(ChatConstants.MSG_TYPE_SINGLE_AUDIO == msgType)
        {
            String encode = Base64.encode(data.toByteArray());
            msg.setContent(encode);
        }else
        {
            msg.setContent(data.toStringUtf8());
        }
        msg.setMsgType(msgType);
        msg.setDisplayType(MsgAnalyzeEngine.getDisPlayType(msg));
        return msg;
    }
}
