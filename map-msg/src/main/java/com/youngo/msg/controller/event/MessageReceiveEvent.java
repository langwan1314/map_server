package com.youngo.msg.controller.event;

import com.youngo.core.model.msg.MessageEntity;
import org.springframework.context.ApplicationEvent;

/**
 * Created by 浮沉 on 2016/3/23.
 * 服务器收到聊天消息事件
 */
public class MessageReceiveEvent extends ApplicationEvent
{
    /**
     * Create a new ApplicationEvent.
     */
    public MessageReceiveEvent(MessageEntity msg)
    {
        super(msg);
    }

    /**
     * The object on which the Event initially occurred.
     *
     * @return   The object on which the Event initially occurred.
     */
    public MessageEntity getSource() {
        return (MessageEntity)source;
    }
}
