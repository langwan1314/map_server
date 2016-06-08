package com.youngo.core.service.msg;

import com.youngo.core.model.msg.ChatConstants;
import com.youngo.core.model.msg.MessageConstant;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMMessage;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 *
 * historical reasons,没有充分利用msgType字段
 * 多端的富文本的考虑
 */
public class MsgAnalyzeEngine
{

    public static int getDisPlayType(MessageEntity msg)
    {
        int msgType = msg.getMsgType();
        if(ChatConstants.MSG_TYPE_SINGLE_AUDIO==msgType)
        {
            return ChatConstants.SHOW_AUDIO_TYPE;
        }
        if(ChatConstants.MSG_TYPE_SINGLE_IMAGE==msgType)
        {
            return ChatConstants.SHOW_IMAGE_TYPE;
        }
        if(ChatConstants.MSG_TYPE_SINGLE_REALTIMEVOICE==msgType)
        {
            return ChatConstants.SHOW_REALTIME_VOICE;
        }
        if(ChatConstants.MSG_TYPE_SINGLE_TEXT==msgType)
        {
            String content = msg.getContent();
            if (content.startsWith(MessageConstant.IMAGE_MSG_START)
                    && content.endsWith(MessageConstant.IMAGE_MSG_END))
            {
                return ChatConstants.SHOW_IMAGE_TYPE;
            }
        }
        return ChatConstants.SHOW_ORIGIN_TEXT_TYPE;
    }

    public static int getMsgType(IMMessage.IMMsgData msgData)
    {
        IMBaseDefine.MsgType msgType = msgData.getMsgType();
        switch (msgType)
        {
            case MSG_TYPE_SINGLE_AUDIO:
            case MSG_TYPE_GROUP_AUDIO:
                return ChatConstants.MSG_TYPE_SINGLE_AUDIO;
            case MSG_TYPE_GROUP_TEXT:
            case MSG_TYPE_SINGLE_TEXT:
                return ChatConstants.MSG_TYPE_SINGLE_TEXT;
            case MSG_TYPE_SINGLE_IMAGE:
            case MSG_TYPE_GROUP_IMAGE:
                return ChatConstants.MSG_TYPE_SINGLE_IMAGE;
            case MSG_TYPE_SINGLE_REALTIMEVOICE:
            case MSG_TYPE_GROUP_REALTIMEVOICE:
                return ChatConstants.MSG_TYPE_SINGLE_REALTIMEVOICE;
            case MSG_TYPE_SINGLE_BUDDYAGREE:
            case MSG_TYPE_GROUP_BUDDYAGREE:
                return ChatConstants.MSG_TYPE_SINGLE_BUDDYAGREE;
            default:
                throw new RuntimeException("ProtoBuf2JavaBean#getMessageEntity wrong type!");
        }
    }

}
