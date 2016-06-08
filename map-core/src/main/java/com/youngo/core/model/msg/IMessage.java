package com.youngo.core.model.msg;

/**
 * Created by fuchen on 2015/11/17.
 * 聊天消息接口
 */
public interface IMessage
{
    /**
     *@return 消息的唯一标识（只在当前会话中唯一)
     */
    int getId();

    /**
     * @return 消息类型，1文本、2图片、3语音、4视频、5时间分割
     */
    int getMsgType();

    /**
     * @return 消息体
     */
    String getContent();
}
